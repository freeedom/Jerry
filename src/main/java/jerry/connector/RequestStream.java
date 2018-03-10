package jerry.connector;

import jerry.Request;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RequestStream extends ServletInputStream
{
    private InputStream socketInputStream;

    private Request request;

    private int length;

    private boolean close=false;



    public RequestStream(Request request) throws IOException
    {
        this.request = request;
        this.socketInputStream=request.getInputStream();
    }

    public int read() throws IOException
    {
        return -1;
    }
}
