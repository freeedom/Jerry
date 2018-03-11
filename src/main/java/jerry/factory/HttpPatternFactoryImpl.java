package jerry.factory;

import java.util.regex.Pattern;

public class HttpPatternFactoryImpl implements HttpPatternFactory
{
    static HttpPatternFactory patternFactory;

    Pattern requestLinePattern;

    Pattern parametersPattern;



    private HttpPatternFactoryImpl(){}

    @Override
    public Pattern getRequestLinePattern()
    {
        if(requestLinePattern==null)
        {
            synchronized (this)
            {
                if(requestLinePattern==null)
                    requestLinePattern=Pattern.compile("[^\\ ]+");
            }
        }
        return requestLinePattern;
    }

    @Override
    public Pattern getParameterPattern()
    {
        if(parametersPattern==null)
        {
            synchronized (this)
            {
                if(parametersPattern==null)
                    parametersPattern=Pattern.compile("([^&=]*)=([^=&]*)");
            }
        }
        return parametersPattern;
    }

    public static HttpPatternFactory getHttpPatternFactory()
    {
        if(patternFactory==null)
        {
            synchronized (HttpPatternFactoryImpl.class)
            {
                if(patternFactory==null)
                    patternFactory=new HttpPatternFactoryImpl();
            }
        }
        return patternFactory;
    }



}
