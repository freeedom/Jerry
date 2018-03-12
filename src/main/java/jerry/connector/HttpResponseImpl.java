package jerry.connector;

import jerry.HttpRequest;
import jerry.HttpResponse;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpResponseImpl extends ResponseImpl implements HttpResponse
{
    private HttpRequest httpRequest;

    private List<Cookie> cookies=new ArrayList<>(8);

    private Map<String,String> headers=new HashMap<>(16);

    protected final SimpleDateFormat format =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.US);

    public HttpResponseImpl(Socket socket,HttpRequest httpRequest)
    {
        super(socket);
        this.httpRequest=httpRequest;
    }

    @Override
    public void addCookie(Cookie cookie)
    {
        cookies.add(cookie);
    }

    @Override
    public boolean containsHeader(String s)
    {
        return headers.containsKey(s);
    }

    @Override
    public String encodeURL(String s)
    {
        String result=null;
        try {
            result= URLEncoder.encode(s,this.getCharacterEncoding());
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String encodeRedirectURL(String s)
    {
        return encodeURL(s);
    }

    @Override
    public String encodeUrl(String s)
    {
        return encodeURL(s);
    }

    @Override
    public String encodeRedirectUrl(String s)
    {
        return encodeURL(s);
    }

    @Override
    public void sendError(int i, String s) throws IOException
    {

    }

    @Override
    public void sendError(int i) throws IOException
    {

    }

    @Override
    public void sendRedirect(String s) throws IOException
    {

    }

    @Override
    public void setDateHeader(String s, long l)
    {
        setHeader(s, format.format(new Date(l)));
    }

    @Override
    public void addDateHeader(String s, long l)
    {
        setHeader(s, format.format(new Date(l)));
    }

    @Override
    public void setHeader(String s, String s1)
    {
        headers.put(s,s1);
    }

    @Override
    public void addHeader(String s, String s1)
    {
        headers.put(s,s1);
    }

    @Override
    public void setIntHeader(String s, int i)
    {
        setHeader(s, "" + i);
    }

    @Override
    public void addIntHeader(String s, int i)
    {
        addHeader(s, "" + i);
    }

    @Override
    public void setStatus(int i)
    {

    }

    @Override
    public void setStatus(int i, String s)
    {

    }

    @Override
    public int getStatus()
    {
        return 0;
    }

    @Override
    public String getHeader(String s)
    {
        return headers.get(s);
    }

    @Override
    public Collection<String> getHeaders(String s)
    {
        List<String> list=new ArrayList<>();
        list.add(headers.get(s));
        return list;
    }

    @Override
    public Collection<String> getHeaderNames()
    {
        return headers.keySet();
    }
}
