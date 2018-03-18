package jerry;

public interface Valve
{
    String getInfo();

    void invoke(HttpRequest httpRequest,HttpResponse response,ValveSupport valveSupport);
}
