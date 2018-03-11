package jerry.connector;

import jerry.HttpRequest;
import jerry.factory.HttpPatternFactory;
import jerry.factory.HttpPatternFactoryImpl;

import java.io.*;
import java.net.Socket;
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

    private HttpPatternFactory patternFactory= HttpPatternFactoryImpl.getHttpPatternFactory();

    private static final byte[] ack = (new String("HTTP/1.1 100 Continue\r\n\r\n")).getBytes();

    private static final byte[] CRLF = (new String("\r\n")).getBytes();




    public HttpProcess(Socket socket) throws IOException
    {
        this.socket = socket;
        socketInputStream=socket.getInputStream();
        socketOutputStream=socket.getOutputStream();
        httpRequest=new HttpRequestImpl(socket);
    }

    String readLine() throws IOException
    {
        StringBuilder sb=new StringBuilder("");
        int ch=-1;
        while((ch=socketInputStream.read())!=-1)
        {
            if((char) ch=='\r') continue;
            else if((char) ch=='\n') break;
            sb.append((char) ch);
        }
        return sb.toString();
    }
    void parseRequest() throws IOException
    {
        parseRequestLine();
        test();
    }

    private void test()
    {
        //test requestLine
        System.out.println("method="+httpRequest.getMethod());
        Enumeration<String> headerEnumeration= httpRequest.getHeaderNames();
        while(headerEnumeration.hasMoreElements())
        {
            String header=headerEnumeration.nextElement();
            System.out.println("header-------------------------");
            System.out.println(header+"="+httpRequest.getHeader(header));
        }
        if(httpRequest.getQueryString()!=null)
        {
            System.out.println("queryString="+httpRequest.getQueryString());
            Enumeration<String> parameterEnumerations= httpRequest.getParameterNames();
            while(parameterEnumerations.hasMoreElements())
            {
                String header=parameterEnumerations.nextElement();
                System.out.println("parameters-------------------------");
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
