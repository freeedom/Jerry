package jerry.parse;

import java.util.HashMap;
import java.util.Map;

public class ContextBean
{
    private String name;
    private int port;
    private String debug;
    private String webroot;
    private Map<String,String> servletNameClassMap=new HashMap<>();
    private Map<String,String> servletNameUrlMap=new HashMap<>();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getDebug()
    {
        return debug;
    }

    public void setDebug(String debug)
    {
        this.debug = debug;
    }

    public String getWebroot()
    {
        return webroot;
    }

    public void setWebroot(String webroot)
    {
        this.webroot = webroot;
    }

    public Map<String, String> getServletNameClassMap()
    {
        return servletNameClassMap;
    }

    public Map<String, String> getServletNameUrlMap()
    {
        return servletNameUrlMap;
    }

    public void setServletNameClass(String servletName,String servletClass)
    {
        servletNameClassMap.put(servletName,servletClass);
    }
    public void setServletNameUrl(String servletName,String urlPattern)
    {
        servletNameUrlMap.put(servletName,urlPattern);
    }
}
