package jerry.connector;

import jerry.Response;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

public class ResponseImpl implements Response
{
    protected String characterEncoding="UTF-8";

    protected String contentType;

    protected int contentLength;

    protected ServletOutputStream servletOutputStream;

    protected PrintWriter printWriter;

    protected int bufferSize=8192;

    protected Socket socket;

    public ResponseImpl(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public String getCharacterEncoding()
    {
        return characterEncoding;
    }

    @Override
    public String getContentType()
    {
        return contentType;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException
    {
       if(printWriter!=null)
           throw new IOException("already getWriter");
       if(servletOutputStream==null)
           servletOutputStream=new ResponseStream(this);
       return servletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException
    {
        if(servletOutputStream!=null)
            throw new IOException("already getOutputStream");
        if(printWriter==null)
            printWriter=new PrintWriter(new ResponseStream(this));
        return printWriter;
    }

    @Override
    public void setCharacterEncoding(String s)
    {
        this.characterEncoding=s;
    }

    @Override
    public void setContentLength(int i)
    {
        this.contentLength=i;
    }

    @Override
    public void setContentType(String s)
    {
        this.contentType=s;
    }

    @Override
    public void setBufferSize(int i)
    {
        this.bufferSize=i;
    }

    @Override
    public int getBufferSize()
    {
        return bufferSize;
    }

    @Override
    public void flushBuffer() throws IOException
    {
        if(servletOutputStream!=null)
            servletOutputStream.flush();
        else printWriter.flush();
    }

    @Override
    public void resetBuffer()
    {

    }

    @Override
    public boolean isCommitted()
    {
        return false;
    }

    @Override
    public void reset()
    {

    }

    @Override
    public void setLocale(Locale locale)
    {

    }

    @Override
    public Locale getLocale()
    {
        return null;
    }

    @Override
    public Socket getSocket()
    {
        return this.socket;
    }
}
