package jerry.container;

import jerry.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class ContextContainerImpl implements Container, ContextContainer
{
    public static final String info="jerry.container.ContextContainerImpl";

    private String contextName;

    private String contextPath;

    private HashMap<String,String> patternMap=new HashMap<>();

    private ArrayList<Container> containers=new ArrayList<>();

    private Chain chain=new ChainImpl(this);

    private Mapper mapper=new MapperImpl(this);

    @Override
    public String getInfo()
    {
        return info;
    }

    @Override
    public String getName()
    {
        return contextName;
    }

    @Override
    public void setName(String name)
    {
        this.contextName=name;
    }

    @Override
    public void addChild(Container container)
    {
        containers.add(container);
    }

    @Override
    public Container getChild(String name)
    {
       return  containers.stream().filter(container -> container.getName().equals(name) ).findFirst().get();
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
        chain.invoke((HttpRequest)request,(HttpResponse)response);
        Container servletContainer= mapper.map((HttpRequest) request);

    }

    @Override
    public void setLoader(Loader loader)
    {

    }

    @Override
    public Loader getLoader()
    {
        return null;
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
        this.contextPath=contextPath;
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
        patternMap.put(pattern,servletName);
    }

    @Override
    public String[] getServletMappings()
    {
        return (String[]) patternMap.keySet().toArray();
    }

    @Override
    public String findServletContainerMapping(String pattern)
    {
        synchronized (this)
        {
            return patternMap.get(pattern);
        }
    }
}
