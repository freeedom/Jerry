package jerry.connector;

import jerry.Contained;
import jerry.Container;
import jerry.HttpRequest;
import jerry.HttpResponse;
import jerry.factory.HttpPatternFactory;
import jerry.factory.HttpPatternFactoryImpl;
import jerry.test.TestHeader;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpProcess implements Runnable, Contained
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

    private Container container;

    private static final byte[] ack = (new String("HTTP/1.1 100 Continue\r\n\r\n")).getBytes();

    private static final byte[] CRLF = (new String("\r\n")).getBytes();




    public HttpProcess(Socket socket,Container container) throws IOException
    {
        this.socket = socket;
        socketInputStream=socket.getInputStream();
        socketOutputStream=socket.getOutputStream();
        httpRequest=new HttpRequestImpl(socket);
        httpResponse=new HttpResponseImpl(socket);
        httpRequest.setHttpResponse(httpResponse);
        httpResponse.setHttpRequest(httpRequest);
        this.container=container;
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
        String requestURL="http://"+httpRequest.getHeader("Host")+httpRequest.getRequestURI();
        httpRequest.setRequestURL(requestURL);
        //http://localhost:8080/servlet/bird
        //http://sdafdsa.com/bird
        String requestURI=httpRequest.getRequestURI();
        if(requestURI.lastIndexOf("/")==0)
        {
            httpRequest.setContextPath("");
        }
        else
        {
            int firstIndex=requestURI.indexOf("/");
            int secondIndex=requestURI.indexOf("/",firstIndex+1);
            String contextPath=requestURI.substring(firstIndex+1,secondIndex);
            httpRequest.setContextPath(contextPath);
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
            container.invoke(httpRequest,httpResponse);
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


    @Override
    public Container getContainer()
    {
        return container;
    }

    @Override
    public void setContainer(Container container)
    {
        this.container=container;
    }
}
