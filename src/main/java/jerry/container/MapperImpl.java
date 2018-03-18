package jerry.container;

import jerry.*;

public class MapperImpl implements Mapper
{

    private final static String info="jerry.container.MapperImpl";

    private ContextContainerImpl context;


    @Override
    public String getInfo()
    {
        return info;
    }

    @Override
    public Container getContainer()
    {
        return context;
    }

    @Override
    public void setContainer(Container container)
    {
        if(!(container instanceof ContextContainerImpl ))
            throw new IllegalArgumentException("the argument is not contextContainerImpl");
        context= (ContextContainerImpl) container;
    }

    @Override
    public Container map(HttpRequest request)
    {
        if(request.getServletContainer()!=null)
            return request.getServletContainer();
        String contextPath=request.getContextPath();
        String relativeURI=request.getRequestURI().substring(contextPath.length());

        ServletContainer servletContainer=null;

        if(servletContainer==null)
        {

        }
        return null;

    }
}
