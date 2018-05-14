package jerry.loader;

import jerry.Container;
import jerry.Loader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class JerryLoader implements Loader
{
    private static Logger logger = Logger.getLogger(JerryLoader.class);

    private static final String info="jerryLoader";

    private ClassLoader classLoader=null;

    private Container container=null;

    private ArrayList<String> repositories=new ArrayList<>();

    public JerryLoader()
    {
        String webroot=System.getProperty("user.dir")+ File.separator+"webroot/classes";
        addRepository(webroot);
        logger.debug(info+" start");
    }

    @Override
    public String getInfo()
    {
        return info;
    }

    @Override
    public ClassLoader getClassLoader()
    {
        if(classLoader==null)
        {
            synchronized (this)
            {
                if(classLoader==null)
                {
                    URL[] urls=new URL[repositories.size()];
                    for(int i=0;i<repositories.size();i++)
                    {
                        urls[i]=repositoryToURL(repositories.get(i));
                    }
                    classLoader=new URLClassLoader(urls);
                }
            }
        }
        return classLoader;
    }

    @Override
    public Container getContainer()
    {
        return container;
    }

    @Override
    public void setContainer(Container container)
    {
        this.container=container;
    }

    @Override
    public void addRepository(String repository)
    {
        repositories.add(repository);
    }

    @Override
    public String[] getRepositories()
    {
        return (String[]) repositories.toArray();
    }

    private URL repositoryToURL(String webroot)
    {
        URL url=null;
        try {
            URLStreamHandler streamHandler=null;
            File classPath=new File(webroot);
            String repository=(new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString() ;
             url=new URL(null,repository,streamHandler);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}
