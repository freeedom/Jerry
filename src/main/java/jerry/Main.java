package jerry;

import jerry.connector.JerryConnector;
import jerry.connector.RequestImpl;
import jerry.container.ContextContainerImpl;
import jerry.container.ServletContainerImpl;

import javax.servlet.ServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Main
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        Connector connector=new JerryConnector();
        Container container=new ContextContainerImpl();
        connector.setContainer(container);
        ServletContainer firstServletContainer=new ServletContainerImpl();
        firstServletContainer.setName("firstServlet");
        container.addChild(firstServletContainer);
        ((ContextContainer)container).addServletMapping("/haha/first","firstServlet");
        ((LifeCycleSubject)connector).start();
        System.in.read();
        ((LifeCycleSubject)connector).stop();
    }
}
