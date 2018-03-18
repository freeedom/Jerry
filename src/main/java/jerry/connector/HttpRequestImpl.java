package jerry.connector;

import jerry.HttpRequest;
import jerry.HttpResponse;
import jerry.ServletContainer;
import jerry.util.Enumerate;
import lombok.Setter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.net.Socket;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpRequestImpl extends RequestImpl implements HttpRequest
{
    private ArrayList<Cookie> cookies=new ArrayList<>();

    private HashMap<String,String> headers=new HashMap<>();

    private String method;

    private String queryString;

    private String requestURI;

    private String requestURL;

    private String contextPath;

    protected SimpleDateFormat formats[] = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
            new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US),
            new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US)
    };

    private HttpResponse httpResponse;

    private ServletContainer servletContainer;

    public HttpRequestImpl(Socket socket)
    {
        super(socket);
    }

    @Override
    public String getAuthType()
    {
        return null;
    }

    @Override
    public Cookie[] getCookies()
    {
        Cookie[] cookiesArray=new Cookie[cookies.size()];
        for(int i=0;i<cookies.size();i++)
        {
            cookiesArray[i]=cookies.get(i);
        }
        return cookiesArray;
    }

    @Override
    public long getDateHeader(String s)
    {
        String value = getHeader(s);
        if (value == null)
            return (-1L);

        // Work around a bug in SimpleDateFormat in pre-JDK1.2b4
        // (Bug Parade bug #4106807)
        value += " ";

        // Attempt to convert the date header in a variety of formats
        for (int i = 0; i < formats.length; i++) {
            try {
                Date date = formats[i].parse(value);
                return (date.getTime());
            } catch (ParseException e) {
                ;
            }
        }
        throw new IllegalArgumentException(value);
    }

    @Override
    public String getHeader(String s)
    {
        return headers.get(s);
    }

    @Override
    public Enumeration<String> getHeaders(String s)
    {
        ArrayList<String> temp=new ArrayList<>();
        String headValue= headers.get(s);
        if(headValue==null) return null;
        temp.add(headValue);
        return new Enumerate<String>(temp.iterator());
    }

    @Override
    public Enumeration<String> getHeaderNames()
    {
        return new Enumerate<String>(headers.keySet().iterator());
    }

    @Override
    public int getIntHeader(String s)
    {
        String value = getHeader(s);
        if (value == null)
            return (-1);
        else
            return (Integer.parseInt(value));
    }

    @Override
    public String getMethod()
    {
        return method;
    }

    @Override
    public String getPathInfo()
    {
        return null;
    }

    @Override
    public String getPathTranslated()
    {
        return null;
    }

    @Override
    public String getContextPath()
    {
        return contextPath;
    }

    @Override
    public String getQueryString()
    {
        return queryString;
    }

    @Override
    public String getRemoteUser()
    {
        return null;
    }

    @Override
    public boolean isUserInRole(String s)
    {
        return false;
    }

    @Override
    public Principal getUserPrincipal()
    {
        return null;
    }

    @Override
    public String getRequestedSessionId()
    {
        return null;
    }

    @Override
    public String getRequestURI()
    {
        return requestURI;
    }

    @Override
    public StringBuffer getRequestURL()
    {
        StringBuffer sb=new StringBuffer(requestURL);
        return sb;
    }

    @Override
    public String getServletPath()
    {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b)
    {
        return null;
    }

    @Override
    public HttpSession getSession()
    {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid()
    {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie()
    {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL()
    {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl()
    {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException
    {
        return false;
    }

    @Override
    public void login(String s, String s1) throws ServletException
    {

    }

    @Override
    public void logout() throws ServletException
    {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException
    {
        return null;
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException
    {
        return null;
    }

    @Override
    public void setMethod(String method)
    {
        this.method=method;
    }

    @Override
    public void setQueryString(String queryString)
    {
        this.queryString=queryString;
    }

    @Override
    public void setRequestURI(String requestURI)
    {
        this.requestURI=requestURI;
    }

    @Override
    public void setRequestURL(String requestURL)
    {
        this.requestURL=requestURL;
    }

    @Override
    public void setHeader(String header, String value)
    {
        headers.put(header,value);
    }

    @Override
    public void setCookie(String key,String value)
    {
        Cookie cookie=new Cookie(key,value);
        cookies.add(cookie);
    }

    @Override
    public void setHttpResponse(HttpResponse httpResponse)
    {
        this.httpResponse=httpResponse;
    }

    @Override
    public void setContextPath(String contextPath)
    {
        this.contextPath=contextPath;
    }

    @Override
    public void setServletContainer(ServletContainer servletContainer)
    {
        this.servletContainer=servletContainer;
    }

    @Override
    public ServletContainer getServletContainer()
    {
        return servletContainer;
    }
}
