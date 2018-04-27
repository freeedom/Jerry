package jerry.connector;

import jerry.HttpResponse;
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

    private boolean writeHeader=false;

    public ResponseStream(Response response) throws IOException
    {
        this.response = response;
        this.socketOutputStream=response.getSocket().getOutputStream();
        this.bufferSize=response.getBufferSize();
        this.buffers=new byte[bufferSize];
        writeHeaders();
    }

    @Override
    public void write(int b) throws IOException
    {
//        if(!writeHeader)
//        {
//            writeHeader=true;
//            writeHeaders();
//        }
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

    private void writeHeaders()
    {
        HttpResponse httpResponse= (HttpResponse) response;
        StringBuilder sb=new StringBuilder("");
        int s=httpResponse.getStatus();
        HttpStatus  status=HttpStatus.getHttpStatus(s);
        sb.append("HTTP/1.1 "+status.getStatus()+" "+status.getName()+"\r\n");
        for (String headerName : httpResponse.getHeaderNames()) {
            String value=httpResponse.getHeader(headerName);
            sb.append(headerName+": "+value+"\r\n");
        }
        sb.append("\r\n");
        try {
            socketOutputStream.write(sb.toString().getBytes(response.getCharacterEncoding()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
