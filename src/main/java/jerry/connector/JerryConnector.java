package jerry.connector;

import jerry.*;
import jerry.factory.ServerSocketFactory;
import jerry.factory.ServerSocketFactoryImpl;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;

import javax.servlet.ServletOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class JerryConnector implements Connector,Runnable, LifeCycleSubject
{
    private static Logger logger = Logger.getLogger(JerryConnector.class);

    private static final String info="jerry.connector.JerryConnector";

    private Container container;

    private jerry.factory.ServerSocketFactory serverSocketFactory;

    private int port=8080;

    private ServerSocket serverSocket;

    private LifeCycleSubjectSupport support=new LifeCycleSubjectSupport(this);

    private Thread thread;

    private int timeOut=15000;

    private volatile boolean start=true;

    private ExecutorService executorService=Executors.newCachedThreadPool();

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
        logger.debug(info+" start");
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

    @Override
    public void setPort(int port)
    {
        this.port=port;
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
                HttpProcess process=new HttpProcess(socket,container);
                executorService.submit(process);
//                process.run();
            }
            catch (IOException e) {
                logger.error("",e);
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
//        System.out.println("connector start");
        logger.debug("connector start");
        support.invokeLifeCycleListeners(event);
    }

    public void stop()
    {
        LifeCycleEvent event=new LifeCycleEvent(this,null,LifeCycleEnum.STOP);
        support.invokeLifeCycleListeners(event);
        start=false;
        executorService.shutdownNow();
        try {
            serverSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("connector stop");
        logger.debug("connector stop");
    }
}
