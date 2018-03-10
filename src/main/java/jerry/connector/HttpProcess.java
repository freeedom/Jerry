package jerry.connector;

import jerry.HttpRequest;
import lombok.Getter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.Socket;
import java.nio.Buffer;

public class HttpProcess implements Runnable
{

    private static final String info="httpProcess";

    private Socket socket;

    private boolean keepAlive = false;

    private boolean http11 = true;

    private boolean sendAck = false;

    private InputStream socketInputStream;

    private OutputStream socketOutputStream;

    private static final byte[] ack = (new String("HTTP/1.1 100 Continue\r\n\r\n")).getBytes();

    private static final byte[] CRLF = (new String("\r\n")).getBytes();


    private HttpRequest httpRequest;


    public HttpProcess(Socket socket) throws IOException
    {
        this.socket = socket;
        socketInputStream=socket.getInputStream();
        socketOutputStream=socket.getOutputStream();
        httpRequest=new HttpRequestImpl(socket);
    }

    String readLine() throws IOException
    {
        StringBuilder sb=new StringBuilder("");
        int ch=-1;
        while((ch=socketInputStream.read())!=-1)
        {
            if((char) ch=='\r') continue;
            else if((char) ch=='\n') break;
            sb.append((char) ch);
        }
        return sb.toString();
    }
    void parseRequest() throws IOException
    {
        System.out.println(readLine());
    }
    void parseRequestLine()
    {

    }


    public void run()
    {
        try {
            parseRequest();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                System.out.println("before socket close");
                socket.close();
                System.out.println("after socket close");

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
