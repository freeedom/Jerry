package jerry.factory;

import java.io.IOException;
import java.net.ServerSocket;

public interface ServerSocketFactory
{

    ServerSocket getServerSocket(int port) throws IOException;
}
