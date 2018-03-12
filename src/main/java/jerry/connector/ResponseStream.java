package jerry.connector;

import jerry.Response;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseStream extends ServletOutputStream
{
    private int bufferSize;

    private byte[] buffers;

    private Response response;

    private OutputStream socketOutputStream;

    private int cur=0;

    private int start=0;

    public ResponseStream(Response response) throws IOException
    {
        this.response = response;
        this.socketOutputStream=response.getSocket().getOutputStream();
        this.bufferSize=response.getBufferSize();
        this.buffers=new byte[bufferSize];
    }

    @Override
    public void write(int b) throws IOException
    {
        if((cur+1)%bufferSize!=start)
        {
            buffers[cur++]= (byte) b;
        }
        else
        {
            flush();
            write(b);
        }

    }

    @Override
    public void flush() throws IOException
    {
        int pos=start-1;
        while((pos+1)%bufferSize!=cur)
        {
            socketOutputStream.write(buffers[++pos]);
        }
        cur=start=0;
    }

    @Override
    public void close() throws IOException
    {
        flush();
    }
}
