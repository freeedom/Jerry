package jerry.test;

import jerry.HttpRequest;
import jerry.HttpResponse;

import javax.servlet.http.Cookie;

public class TestCookie
{
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    public TestCookie(HttpRequest httpRequest, HttpResponse httpResponse)
    {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    public void test()
    {
        System.out.println("----------testCookie----------------------------");
        for (Cookie cookie : httpRequest.getCookies()) {
            System.out.println(cookie.getName()+"--"+cookie.getValue());
        }
    }
}
