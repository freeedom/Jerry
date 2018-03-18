package jerry;

import javax.servlet.ServletContext;

public interface ContextContainer extends Container
{
    String getContextPath();

    void setContextPath(String contextPath);

    ServletContext getServletContext();

    int getSessionTimeout();

    void setSessionTimeout(int timeout);

    void addServletMapping(String pattern,String servletName);

    String[] getServletMappings();

    String findServletContainerMapping(String pattern);


}
