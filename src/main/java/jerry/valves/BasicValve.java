package jerry.valves;

import jerry.*;

public class BasicValve implements Valve
{
    private Container container;

    public BasicValve(Container container)
    {
        this.container = container;
    }

    @Override
    public String getInfo()
    {
        return "basic valve";
    }

    @Override
    public void invoke(HttpRequest httpRequest, HttpResponse httpResponse, ValveSupport valveSupport)
    {
        container.invoke(httpRequest,httpResponse);
    }
}
