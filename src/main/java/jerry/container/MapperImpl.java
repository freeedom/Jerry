package jerry.container;

import jerry.*;
import jerry.util.URLUtil;

import java.sql.Wrapper;
import java.util.List;

public class MapperImpl implements Mapper
{

    private final static String info = "jerry.container.MapperImpl";

    private ContextContainerImpl container;

    public MapperImpl(ContextContainerImpl container)
    {
        this.container = container;
    }

    @Override
    public String getInfo()
    {
        return info;
    }

    @Override
    public Container getContainer()
    {
        return container;
    }

    @Override
    public void setContainer(Container container)
    {
        if (!(container instanceof ContextContainerImpl))
            throw new IllegalArgumentException("the argument is not contextContainerImpl");
        container = (ContextContainerImpl) container;
    }

    @Override
    public Container map(HttpRequest request)
    {
        if (request.getServletContainer() != null) return request.getServletContainer();
        String contextPath = request.getContextPath();
        String relativeURI = request.getRequestURI().substring(contextPath.length()+1);
        ServletContainer servletContainer = null;
        String name = null;
        if (servletContainer == null) {
            name = container.findServletContainerMapping(relativeURI);
            if (name != null) {
                servletContainer = (ServletContainer) container.getChild(name);
            }
        }

        if (servletContainer == null) {
            while (true) {
                name = container.findServletContainerMapping(relativeURI);
                if (name != null) {
                    servletContainer = (ServletContainer) container.getChild(name);
                }
                if (servletContainer != null) {
                    break;
                }
                int index = relativeURI.lastIndexOf('/');
                if (index < 0 || relativeURI.indexOf('.')!=-1) break;
                relativeURI = relativeURI.substring(0, index);
            }
        }

        if (servletContainer == null && request.getRequestURI().indexOf('.')==-1) {
            relativeURI = request.getRequestURI().substring(contextPath.length()+1);
            List<String> urlList=URLUtil.splitUrl(relativeURI);
            urlList.add("/");
            for(int i=0;i<urlList.size();i++)
            {
                name = container.findServletContainerMapping(urlList.get(i));
                if (name != null) {
                    servletContainer = (ServletContainer) container.getChild(name);
                }
                if (servletContainer != null) {
                    break;
                }
            }
        }
        return servletContainer;
    }
}
