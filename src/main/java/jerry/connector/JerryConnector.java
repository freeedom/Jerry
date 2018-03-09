package jerry.connector;

import jerry.*;
import jerry.factory.ServerSocketFactory;
import jerry.factory.ServerSocketFactoryImpl;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class JerryConnector implements Connector,Runnable, LifeCycleSubject
{
    private static final String info="jerry.connector.JerryConnector";

    private Container container;

    private jerry.factory.ServerSocketFactory serverSocketFactory;

    @Getter
    @Setter
    private int port=8080;

    private ServerSocket serverSocket;

    private LifeCycleSubjectSupport support=new LifeCycleSubjectSupport(this);

    private Thread thread;

    @Setter
    @Getter
    private int timeOut;

    private volatile boolean start=true;

    public JerryConnector()
    {
        init();
    }

    public String getInfo()
    {
        return info;
    }

    public Container getContainer()
    {
        return container;
    }

    public void setContainer(Container container)
    {
        this.container=container;

    }

    public ServerSocketFactory getServerSocketFactory()
    {
        if(serverSocketFactory==null)
        {
            serverSocketFactory=new ServerSocketFactoryImpl();
        }
        return serverSocketFactory;
    }

    public void setServerSocketFactory(ServerSocketFactory serverSocketFactory)
    {
        this.serverSocketFactory=serverSocketFactory;
    }

    public void init()
    {
        try {
            serverSocket=getServerSocketFactory().getServerSocket(port);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        while(start)
        {
            Socket socket=null;
            try {
                socket=serverSocket.accept();
                if(timeOut>0)
                    socket.setSoTimeout(timeOut);
                socket.setTcpNoDelay(true);

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(serverSocket!=null)
        {
            try {
                serverSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addLifeCycleListener(LifeCycleListener listener)
    {
        support.addLifeCycleListener(listener);
    }

    public List<LifeCycleListener> getAllLifeCycleListeners()
    {
        return support.getAllLifeCycleListeners();
    }

    public void removeLifeCycleListener(LifeCycleListener listener)
    {
        support.removeLifeCycleListener(listener);
    }

    public void start()
    {
        LifeCycleEvent event=new LifeCycleEvent(this,null,LifeCycleEnum.START);
        thread=new Thread(this,"JerryConnecter thread");
        thread.setDaemon(true);
        thread.start();
        System.out.println("connector start");
        support.invokeLifeCycleListeners(event);
    }

    public void stop()
    {
        LifeCycleEvent event=new LifeCycleEvent(this,null,LifeCycleEnum.STOP);
        support.invokeLifeCycleListeners(event);
        start=false;
        System.out.println("connector stop");
    }
}
