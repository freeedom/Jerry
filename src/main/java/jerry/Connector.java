package jerry;

import jerry.factory.*;


public interface Connector
{
    String getInfo();

    Container getContainer();

    void setContainer(Container container);

    ServerSocketFactory getServerSocketFactory();

    void setServerSocketFactory(ServerSocketFactory serverSocketFactory);

    void init();

    void setPort(int port);


}
