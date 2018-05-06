package jerry.container;

import com.sun.xml.internal.bind.v2.TODO;
import jerry.*;
import jerry.valves.BasicValve;
import jerry.valves.ErrorValve;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.HashMap;

public class ContextContainerImpl implements Container, ContextContainer, Chain
{
    public static final String info = "jerry.container.ContextContainerImpl";

    private String contextName;

    private String contextPath;

    private HashMap<String, String> patternMap = new HashMap<>();

    private ArrayList<Container> containers = new ArrayList<>();

    private Mapper mapper = new MapperImpl(this);

    private ArrayList<Valve> valves = new ArrayList<>();

    private Loader loader;

    @Override
    public String getInfo()
    {
        return info;
    }

    @Override
    public Valve getBasicValve()
    {
        return null;
    }

    @Override
    public void setBasicValve(Valve valve)
    {

    }

    @Override
    public void addValve(Valve valve)
    {
        valves.add(valve);
    }

    @Override
    public Valve[] getValves()
    {
        return (Valve[]) valves.toArray();
    }

    @Override
    public void invoke(HttpRequest httpRequest, HttpResponse httpResponse)
    {

    }


    @Override
    public String getName()
    {
        return contextName;
    }

    @Override
    public void setName(String name)
    {
        this.contextName = name;
    }

    @Override
    public void addChild(Container container)
    {
        containers.add(container);
    }

    @Override
    public Container getChild(String name)
    {
        return containers.stream().filter(container -> container.getName().equals(name)).findFirst().get();
    }

    @Override
    public Container[] getChilds()
    {
        return (Container[]) containers.toArray();
    }

    @Override
    public void setParent(Container container)
    {

    }

    @Override
    public Container getParent()
    {
        return null;
    }

    @Override
    public void invoke(HttpServletRequest request, HttpServletResponse response)
    {
        Container servletContainer = mapper.map((HttpRequest) request);
        Chain chain = new ChainImpl(this);
        if (servletContainer != null) {
            Valve basicValve = new BasicValve(servletContainer);
            chain.setBasicValve(basicValve);
        }
        for (Valve valve : valves) {
            chain.addValve(valve);
        }
        if (servletContainer == null)
        {
            chain.addValve(new ErrorValve());
        }
        chain.invoke((HttpRequest) request, (HttpResponse) response);

    }

    @Override
    public void setLoader(Loader loader)
    {
        this.loader = loader;
    }

    @Override
    public Loader getLoader()
    {
        return loader;
    }

    @Override
    public Loader getParentLoader()
    {
        return null;
    }

    @Override
    public String getContextPath()
    {
        return contextPath;
    }

    @Override
    public void setContextPath(String contextPath)
    {
        this.contextPath = contextPath;
    }

    @Override
    public ServletContext getServletContext()
    {
        return null;
    }

    @Override
    public int getSessionTimeout()
    {
        return 0;
    }

    @Override
    public void setSessionTimeout(int timeout)
    {

    }

    @Override
    public void addServletMapping(String pattern, String servletName)
    {
        patternMap.put(pattern, servletName);
    }

    @Override
    public String[] getServletMappings()
    {
        return (String[]) patternMap.keySet().toArray();
    }

    @Override
    public String findServletContainerMapping(String pattern)
    {
        synchronized (this) {
            return patternMap.get(pattern);
        }
    }
}
