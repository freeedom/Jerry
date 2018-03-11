package jerry;

import javax.servlet.http.HttpServletRequest;

public interface HttpRequest extends HttpServletRequest,Request
{
     void setMethod(String method);

    void setQueryString(String queryString);

    void setRequestURI(String requestURI);

    void setRequestURL(String requestURL);

}
