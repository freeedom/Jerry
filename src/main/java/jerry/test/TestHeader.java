package jerry.test;

import jerry.HttpRequest;
import jerry.HttpResponse;

import java.util.EnumMap;
import java.util.Enumeration;

public class TestHeader
{
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    public TestHeader(HttpRequest httpRequest, HttpResponse httpResponse)
    {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    public void test()
    {
        System.out.println("----------------testHeader-------------------");
        Enumeration<String> enumeration= httpRequest.getHeaderNames();
        while(enumeration.hasMoreElements())
        {
            String name= enumeration.nextElement();
            String value=httpRequest.getHeader(name);
            System.out.println(name+"---"+value);
        }
        System.out.println(httpRequest.getRequestURI());
        System.out.println(httpRequest.getRequestURL());
        System.out.println(httpRequest.getContextPath());
    }
}
