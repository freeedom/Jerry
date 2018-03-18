package jerry;

public interface Chain
{
    String getInfo();

    Valve getBasicValve();

    void setBasicValve(Valve valve);

    void addValve(Valve valve);

    Valve[] getValves();

    void invoke(HttpRequest httpRequest,HttpResponse httpResponse);
}
