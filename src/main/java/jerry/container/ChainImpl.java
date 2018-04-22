package jerry.container;

import jerry.*;

import java.util.ArrayList;

public class ChainImpl implements Chain, Contained
{
    public static final String info="chainImpl";

    private Valve basicValve;

    private ArrayList<Valve> valves=new ArrayList<>();

    private Container container;


    public ChainImpl()
    {
        this(null);
    }

    public ChainImpl(Container container)
    {
        this.container=container;
    }

    @Override
    public String getInfo()
    {
        return info;
    }

    @Override
    public Valve getBasicValve()
    {
        return basicValve;
    }

    @Override
    public void setBasicValve(Valve valve)
    {
        this.basicValve=valve;
    }

    @Override
    public void addValve(Valve valve)
    {
        valves.add(valve);
    }

    @Override
    public Valve[] getValves()
    {
        return (Valve[]) valves.toArray();
    }

    @Override
    public void invoke(HttpRequest httpRequest, HttpResponse httpResponse)
    {
        new ValveSupportImpl().invokeNext(httpRequest,httpResponse);
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

    protected class  ValveSupportImpl implements ValveSupport
    {
        public static final String info="valveSupportImpl";

        private int index=0;


        @Override
        public String getInfo()
        {
            return null;
        }

        @Override
        public void invokeNext(HttpRequest httpRequest, HttpResponse httpResponse)
        {
            if(index<valves.size())
            {
                int temp=index;
                index++;
                valves.get(temp).invoke(httpRequest,httpResponse,this);
            }
            else if(index==valves.size()&&basicValve!=null)
            {
                basicValve.invoke(httpRequest,httpResponse,this);
            }

        }
    }
}
