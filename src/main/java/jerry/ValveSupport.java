package jerry;

public interface ValveSupport
{
    String getInfo();

    void invokeNext(HttpRequest httpRequest,HttpResponse httpResponse);
}
