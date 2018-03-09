package jerry;

import java.util.List;

public interface LifeCycleSubject
{
    void addLifeCycleListener(LifeCycleListener listener);

    List<LifeCycleListener> getAllLifeCycleListeners();

    void removeLifeCycleListener(LifeCycleListener listener);

    void start();

    void stop();
}
