package jerry;

import java.util.ArrayList;
import java.util.List;

public class LifeCycleSubjectSupport
{
    private LifeCycleSubject subject;

    private ArrayList<LifeCycleListener> listeners=new ArrayList<LifeCycleListener>();

    public LifeCycleSubjectSupport(LifeCycleSubject subject)
    {
        this.subject=subject;
    }

    public void addLifeCycleListener(LifeCycleListener listener)
    {
        listeners.add(listener);
    }

    public List<LifeCycleListener> getAllLifeCycleListeners()
    {
        return listeners;
    }

    public void removeLifeCycleListener(LifeCycleListener listener)
    {
        listeners.remove(listener);
    }

    public void invokeLifeCycleListeners(LifeCycleEvent event)
    {
        for(int i=0;i<listeners.size();i++)
        {
            listeners.get(i).update(event);
        }
    }
}
