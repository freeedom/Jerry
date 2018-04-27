package jerry.valves;

import jerry.HttpRequest;
import jerry.HttpResponse;
import jerry.Valve;
import jerry.ValveSupport;

import java.io.IOException;

public class ErrorValve implements Valve
{
    @Override
    public String getInfo()
    {
        return "error valve";
    }

    @Override
    public void invoke(HttpRequest httpRequest, HttpResponse httpResponse, ValveSupport valveSupport)
    {
        httpResponse.setStatus(404);
        try {
            httpResponse.getWriter().write("the url can not reach");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
