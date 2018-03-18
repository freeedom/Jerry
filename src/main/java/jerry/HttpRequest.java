package jerry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public interface HttpRequest extends HttpServletRequest,Request
{
     void setMethod(String method);

    void setQueryString(String queryString);

    void setRequestURI(String requestURI);

    void setRequestURL(String requestURL);

    void setHeader(String header,String value);

    void setCookie(String key,String value);

    void setHttpResponse(HttpResponse httpResponse);

    void setContextPath(String contextPath);

    void setServletContainer(ServletContainer servletContainer);

    ServletContainer getServletContainer();

}
