package jerry;

import javax.servlet.http.HttpServletResponse;

public interface HttpResponse extends HttpServletResponse
{
    void setHttpRequest(HttpRequest httpRequest);

}
