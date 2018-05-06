package jerry.parse;

import java.util.ArrayList;
import java.util.List;

public class WebAppBean
{
    private List<ContextBean> contextBeanList =new ArrayList<>();
    public void addContextBean(ContextBean contextBean)
    {
        contextBeanList.add(contextBean);
    }

    public ContextBean getContextBean(int index)
    {
        return contextBeanList.get(index);
    }
}
