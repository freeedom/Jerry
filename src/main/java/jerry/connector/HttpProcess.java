package jerry.connector;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jerry.HttpRequest;
import jerry.HttpResponse;
import jerry.factory.HttpPatternFactory;
import jerry.factory.HttpPatternFactoryImpl;

import javax.servlet.http.Cookie;
import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpProcess implements Runnable
{

    private static final String info="httpProcess";

    private Socket socket;

    private boolean keepAlive = false;

    private boolean http11 = true;

    private boolean sendAck = false;

    private InputStream socketInputStream;

    private OutputStream socketOutputStream;

    private HttpRequest httpRequest;

    private HttpResponse httpResponse;

    private HttpPatternFactory patternFactory= HttpPatternFactoryImpl.getHttpPatternFactory();

    private static final byte[] ack = (new String("HTTP/1.1 100 Continue\r\n\r\n")).getBytes();

    private static final byte[] CRLF = (new String("\r\n")).getBytes();




    public HttpProcess(Socket socket) throws IOException
    {
        this.socket = socket;
        socketInputStream=socket.getInputStream();
        socketOutputStream=socket.getOutputStream();
        httpRequest=new HttpRequestImpl(socket);
        httpResponse=new HttpResponseImpl(socket);
        httpRequest.setHttpResponse(httpResponse);
        httpResponse.setHttpRequest(httpRequest);
    }

    String readLine() throws IOException
    {
        StringBuilder sb=new StringBuilder("");
        int ch=-1;
        boolean isBlank=false;
        int len=0;
        while((ch=socketInputStream.read())!=-1)
        {
            if((char) ch=='\r')
            {
                if(len==0) isBlank=true;
                continue;
            }
            else if((char) ch=='\n') break;
            sb.append((char) ch);
            len++;
        }
        if(isBlank) return "-1";
        return sb.toString();
    }

    String readPost(int len) throws IOException
    {
        byte[] chars=new byte[len];
        socketInputStream.read(chars,0,len);
        String s=new String(chars,0,len,httpRequest.getCharacterEncoding());
        return URLDecoder.decode(s,httpRequest.getCharacterEncoding());
    }
    void parseRequest() throws IOException
    {
        parseRequestLine();
        parseHeader();
        setHeaderAttribute();
//        testParseRequestLine();
//        testParseHeader();
    }

    void setHeaderAttribute()
    {
        if(httpRequest.getMethod().toUpperCase().equals("POST"))
        {
            String value=httpRequest.getHeader("Content-Type");
            httpRequest.setContentType(value);
            value=httpRequest.getHeader("Content-Length");
            httpRequest.setContentLength(Integer.valueOf(value));
        }
    }


    void parseHeader() throws IOException
    {
        String str="";
        while ((str=readLine())!="")
        {
            //blank line
            if(str=="-1")
                break;
            Pattern headerPattern=patternFactory.getHeaderPattern();
            Matcher headerMatcher=headerPattern.matcher(str);
            String header,value;
            if(headerMatcher.find())
            {
                header=headerMatcher.group(1);
                value=headerMatcher.group(2);
                httpRequest.setHeader(header,value);
                if(header.equals("Cookie"))
                {
                    Pattern cookiePattern=patternFactory.getCookiePattern();
                    Matcher cookieMatcher=cookiePattern.matcher(value);
                    String cookieKey,cookieValue;
                    while(cookieMatcher.find())
                    {
                        cookieKey=cookieMatcher.group("name");
                        cookieValue=cookieMatcher.group("value");
                        httpRequest.setCookie(cookieKey,cookieValue);
                    }
                }
            }
        }
    }

    private void testParseHeader()
    {
        System.out.println("cookie------------");
        Cookie[] cookies=httpRequest.getCookies();
        System.out.println(cookies.length);
        for(int i=0;i<cookies.length;i++)
        {
            System.out.println(cookies[i].getName()+"--"+cookies[i].getValue());
        }
        System.out.println("---------------------");
       Enumeration<String> headerNames=  httpRequest.getHeaderNames();
       while(headerNames.hasMoreElements())
       {
           String key=headerNames.nextElement();
           String value=httpRequest.getHeader(key);
           System.out.println(key+"--"+value);
       }

    }

    private void testParseRequestLine()
    {
        //test requestLine
        System.out.println("method="+httpRequest.getMethod());
        if(httpRequest.getQueryString()!=null)
        {
            System.out.println("queryString="+httpRequest.getQueryString());
            System.out.println("parameters-------------------------");
            Enumeration<String> parameterEnumerations= httpRequest.getParameterNames();
            while(parameterEnumerations.hasMoreElements())
            {
                String header=parameterEnumerations.nextElement();
                System.out.println(header+"="+httpRequest.getParameter(header));
            }
        }
        System.out.println(httpRequest.getProtocol());

    }

    void parseRequestLine() throws IOException
    {
        String str=readLine();
        Pattern pattern=patternFactory.getRequestLinePattern();
        Matcher matcher= pattern.matcher(str);
        int condition=0;
        while (matcher.find())
        {
            String arg= matcher.group();
            switch (condition)
            {
                case 0:
                    httpRequest.setMethod(arg.toUpperCase());
                    break;
                case 1:
                    if(arg.contains("?"))
                    {
                        String queryString=arg.substring(arg.indexOf('?')+1);
                        queryString=URLDecoder.decode(queryString,httpRequest.getCharacterEncoding());
                        arg=arg.substring(0,arg.indexOf('?'));
                        httpRequest.setQueryString(queryString);
                        Pattern parametersPattern=patternFactory.getParameterPattern();
                        Matcher parametersMatch= parametersPattern.matcher(queryString);
                        while(parametersMatch.find())
                        {
                            String key=parametersMatch.group(1);
                            String value=parametersMatch.group(2);
                            httpRequest.setParameter(key,value);
                        }
                    }
                    httpRequest.setRequestURI(arg);
                    break;
                case 2:
                    httpRequest.setProtocol(arg);
                    break;
            }
            condition++;
        }
    }


    public void run()
    {
        try {
            parseRequest();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
