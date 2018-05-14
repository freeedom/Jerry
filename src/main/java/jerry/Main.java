package jerry;

import com.sun.xml.internal.bind.v2.TODO;
import jerry.connector.JerryConnector;
import jerry.container.ContextContainerImpl;
import jerry.container.ServletContainerImpl;
import jerry.parse.ContextBean;
import jerry.parse.ParseXml;
import jerry.parse.WebAppBean;
import jerry.loader.JerryLoader;
import jerry.util.URLUtil;
import jerry.valves.LogValve;
import jerry.valves.StaticResourceValve;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        WebAppBean webAppBean = ParseXml.parse("webroot/server.xml");
        ContextBean contextBean = webAppBean.getContextBean(0);
        Connector connector = new JerryConnector();
        connector.setPort(contextBean.getPort());
        Container container = new ContextContainerImpl();
        connector.setContainer(container);
        Loader loader = new JerryLoader();
//        loader.addRepository("/home/jmt/IdeaProjects/Jerry/webroot");
        loader.addRepository(contextBean.getWebroot());
        container.setLoader(loader);
        ((Chain) container).addValve(new LogValve());
        ((Chain) container).addValve(new StaticResourceValve(container));

        Map<String, String> nameClassMap = contextBean.getServletNameClassMap();
        Map<String, String> nameUrlPattern = contextBean.getServletNameUrlMap();

        for (String name : nameClassMap.keySet()) {
            ServletContainer servletContainer = new ServletContainerImpl();
            servletContainer.setName(name);
            String servletClass = nameClassMap.get(name);
            servletContainer.setServletClass(servletClass);
            servletContainer.setParent(container);
            container.addChild(servletContainer);
            String urlPattern = nameUrlPattern.get(name);
            List<String> urlPatterns = URLUtil.splitUrl(urlPattern);
            urlPatterns.stream().forEach(url ->
            {
                ((ContextContainer) container).addServletMapping(url, name);
            });
//            ((ContextContainer) container).addServletMapping(urlPattern, name);
        }


//        ServletContainer firstServletContainer = new ServletContainerImpl();
//        firstServletContainer.setName("firstServlet");
//        firstServletContainer.setServletClass("FirstServlet");
//        firstServletContainer.setParent(container);
//        container.addChild(firstServletContainer);
//        ((ContextContainer) container).addServletMapping("/haha/first", "firstServlet");
//
//        ServletContainer hello = new ServletContainerImpl();
//        hello.setName("helloServlet");
//        hello.setServletClass("Two");
//        hello.setParent(container);
//        container.addChild(hello);
//        ((ContextContainer) container).addServletMapping("/hello/*", "helloServlet");


        ((LifeCycleSubject) connector).start();
        System.in.read();
        ((LifeCycleSubject) connector).stop();
    }
}
