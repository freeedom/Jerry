package jerry.connector;

import jerry.Request;
import jerry.util.Enumerate;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.*;

public class RequestImpl implements Request
{

    protected HashMap<String,Object> attrbutes=new HashMap<String, Object>();

    protected HashMap<String,String> parameters=new HashMap<>();

    protected String characterEncoding="UTF-8";

    protected String contentType;

    protected int contentLength;

    protected Socket socket;

    protected String protocol;

    protected String scheme="HTTP";

    protected ServletInputStream servletInputStream;

    public RequestImpl(Socket socket)
    {
        this.socket = socket;
    }


    public Object getAttribute(String s)
    {
        synchronized (this)
        {
            return attrbutes.get(s);
        }
    }

    public Enumeration<String> getAttributeNames()
    {
        synchronized (this)
        {
            return new Enumerate(attrbutes.keySet().iterator());
        }
    }

    public String getCharacterEncoding()
    {
        return characterEncoding;
    }


    public void setCharacterEncoding(String s) throws UnsupportedEncodingException
    {
        characterEncoding=s;
    }

    public int getContentLength()
    {
        return contentLength;
    }

    public String getContentType()
    {
        return contentType;
    }

    public ServletInputStream getInputStream() throws IOException
    {
        if(servletInputStream==null)
        {
            synchronized (this)
            {
                if(servletInputStream==null)
                {
                    servletInputStream=new RequestStream(this);
                }
            }
        }
        return servletInputStream;
    }

    public String getParameter(String s)
    {
        return parameters.get(s);
    }

    public Enumeration<String> getParameterNames()
    {
        return new Enumerate<String>(parameters.keySet().iterator());
    }

    public String[] getParameterValues(String s)
    {
        return (String[]) parameters.values().toArray();
    }

    public Map<String, String[]> getParameterMap()
    {
        HashMap<String,String[]> temps=new HashMap<>(parameters.size());
        parameters.keySet().stream().forEach(key->{
            String[] s=new String[1];
            s[0]=parameters.get(key);
            temps.put(key,s);
        });
        return temps;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public String getScheme()
    {
        return scheme;
    }

    public String getServerName()
    {
        return "Jerry";
    }

    public int getServerPort()
    {
        return socket.getLocalPort();
    }

    public BufferedReader getReader() throws IOException
    {
        return null;
    }

    public String getRemoteAddr()
    {
        return socket.getInetAddress().getHostAddress();
    }

    public String getRemoteHost()
    {
        return socket.getInetAddress().getHostName();
    }

    public void setAttribute(String s, Object o)
    {
        attrbutes.put(s,o);
    }

    public void removeAttribute(String s)
    {
        attrbutes.remove(s);
    }

    public Locale getLocale()
    {
        return null;
    }

    public Enumeration<Locale> getLocales()
    {
        return null;
    }

    public boolean isSecure()
    {
        return false;
    }

    public RequestDispatcher getRequestDispatcher(String s)
    {
        return null;
    }

    public String getRealPath(String s)
    {
        return null;
    }

    public int getRemotePort()
    {
        return socket.getPort();
    }

    public String getLocalName()
    {
        return socket.getLocalAddress().getHostName();
    }

    public String getLocalAddr()
    {
        return socket.getLocalAddress().getHostAddress();
    }

    public int getLocalPort()
    {
        return socket.getLocalPort();
    }

    public ServletContext getServletContext()
    {
        return null;
    }

    public AsyncContext startAsync() throws IllegalStateException
    {
        return null;
    }

    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException
    {
        return null;
    }

    public boolean isAsyncStarted()
    {
        return false;
    }

    public boolean isAsyncSupported()
    {
        return false;
    }

    public AsyncContext getAsyncContext()
    {
        return null;
    }

    public DispatcherType getDispatcherType()
    {
        return null;
    }

    @Override
    public void setParameter(String key, String value)
    {
        parameters.put(key,value);
    }

    @Override
    public void setProtocol(String protocol)
    {
        this.protocol=protocol;
    }

    @Override
    public void setContentLength(int contentLength)
    {
        this.contentLength=contentLength;
    }

    @Override
    public void setContentType(String contentType)
    {
        this.contentType=contentType;
    }

    @Override
    public Socket getSocket()
    {
        return this.socket;
    }
}
