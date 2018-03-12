package jerry.connector;

import jerry.HttpRequest;
import jerry.Request;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RequestStream extends ServletInputStream
{
    private InputStream socketInputStream;

    private Request request;

    private int length;

    private int alreadyRead=0;

    public RequestStream(Request request) throws IOException
    {
        this.request = request;
        this.socketInputStream=request.getSocket().getInputStream();
        this.length=request.getContentLength();
    }

    public int read() throws IOException
    {
        if(alreadyRead<length)
        {
            alreadyRead++;
            return socketInputStream.read();
        }
        return -1;
    }

    @Override
    public void close() throws IOException
    {

    }
}
