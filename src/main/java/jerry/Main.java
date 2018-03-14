package jerry;

import jerry.connector.JerryConnector;
import jerry.connector.RequestImpl;

import javax.servlet.ServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Main
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        Connector connector=new JerryConnector();
        ((LifeCycleSubject)connector).start();
        System.in.read();
        ((LifeCycleSubject)connector).stop();
    }
}
