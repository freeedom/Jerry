package jerry;

import lombok.Data;

@Data
public class LifeCycleEvent
{
    private LifeCycleSubject subject;
    private Object data;
    private LifeCycleEnum lifeCycleEnum;

    public LifeCycleEvent(LifeCycleSubject subject, Object data, LifeCycleEnum lifeCycleEnum)
    {
        this.subject = subject;
        this.data = data;
        this.lifeCycleEnum = lifeCycleEnum;
    }
}
