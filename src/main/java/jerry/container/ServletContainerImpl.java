package jerry.container;

import jerry.Container;
import jerry.ContextContainer;
import jerry.Loader;
import jerry.ServletContainer;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletContainerImpl implements ServletContainer
{

    private final static String info="servletContainerImpl";

    private String name;

    private ContextContainer contextContainer;

    private Servlet servlet;

    private String servletClassName;

    private Loader loader;

    private boolean isLoad=false;

    @Override
    public String getInfo()
    {
        return info;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name=name;
    }

    @Override
    public void addChild(Container container)
    {
        throw new IllegalStateException("servletContainer has not child");
    }

    @Override
    public Container getChild(String name)
    {
        throw new IllegalStateException("servletContainer has not child");
    }

    @Override
    public Container[] getChilds()
    {
        throw new IllegalStateException("servletContainer has not child");
    }

    @Override
    public void setParent(Container container)
    {
        this.contextContainer= (ContextContainer) container;
    }

    @Override
    public Container getParent()
    {
        return contextContainer;
    }

    @Override
    public void invoke(HttpServletRequest request, HttpServletResponse response)
    {
        if(isLoad==false)
        {
            try {
                load();
                servlet.init(null);
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (InstantiationException e) {
                e.printStackTrace();
            }
            catch (ServletException e) {
                e.printStackTrace();
            }
        }
        try {
            servlet.service(request,response);
            response.flushBuffer();
        }
        catch (ServletException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setLoader(Loader loader)
    {
        this.loader=loader;
    }

    @Override
    public Loader getLoader()
    {
        if(loader!=null)
            return loader;
        if(contextContainer!=null) {
            loader = contextContainer.getLoader();
            return loader;
        }
        return null;
    }

    @Override
    public Loader getParentLoader()
    {
        return contextContainer.getLoader();
    }

    @Override
    public String getServletClass()
    {
        return servletClassName;
    }

    @Override
    public void setServletClass(String servletClass)
    {
        servletClassName=servletClass;
    }

    @Override
    public void load() throws IllegalAccessException, InstantiationException
    {
        Loader tempLoader=getLoader();
        ClassLoader classLoader=tempLoader.getClassLoader();
        try {
            servlet= (Servlet) classLoader.loadClass(getServletClass()).newInstance();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
