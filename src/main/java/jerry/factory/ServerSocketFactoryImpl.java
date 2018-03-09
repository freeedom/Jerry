package jerry.factory;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketFactoryImpl implements ServerSocketFactory
{
    private ServerSocket serverSocket;

    public ServerSocket getServerSocket(int port) throws IOException
    {
        if(serverSocket==null)
        {
            synchronized (this)
            {
                if(serverSocket==null)
                {
                    serverSocket=new ServerSocket(port);
                }
            }
        }
        return serverSocket;
    }
}
