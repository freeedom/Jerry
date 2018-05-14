package jerry.valves;

import jerry.HttpRequest;
import jerry.HttpResponse;
import jerry.Valve;
import jerry.ValveSupport;
import org.apache.log4j.Logger;

import java.util.Enumeration;

public class LogValve implements Valve
{
    private static Logger logger = Logger.getLogger(LogValve.class);

    @Override
    public String getInfo()
    {
        return "logValve";
    }

    @Override
    public void invoke(HttpRequest httpRequest, HttpResponse httpResponse, ValveSupport valveSupport)
    {
        logger.debug("---------------------------request--------------------------------");
        logger.debug("URL:"+httpRequest.getRequestURL());
        logger.debug("------------------------------------------------------------------");
        valveSupport.invokeNext(httpRequest,httpResponse);
    }
}
