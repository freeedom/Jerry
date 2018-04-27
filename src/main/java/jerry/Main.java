package jerry;

import jerry.connector.JerryConnector;
import jerry.connector.RequestImpl;
import jerry.container.ContextContainerImpl;
import jerry.container.ServletContainerImpl;
import jerry.loader.JerryLoader;
import jerry.valves.StaticResourceValve;

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
        Loader loader=new JerryLoader();
        loader.addRepository("/home/jmt/IdeaProjects/Jerry/webroot");
        container.setLoader(loader);
        ((Chain)container).addValve(new StaticResourceValve(container));


        ServletContainer firstServletContainer=new ServletContainerImpl();
        firstServletContainer.setName("firstServlet");
        firstServletContainer.setServletClass("FirstServlet");
        firstServletContainer.setParent(container);
        container.addChild(firstServletContainer);
        ((ContextContainer)container).addServletMapping("/haha/first","firstServlet");

        ServletContainer hello=new ServletContainerImpl();
        hello.setName("helloServlet");
        hello.setServletClass("Two");
        hello.setParent(container);
        container.addChild(hello);
        ((ContextContainer)container).addServletMapping("/hello","helloServlet");



        ((LifeCycleSubject)connector).start();
        System.in.read();
        ((LifeCycleSubject)connector).stop();
    }
}
