import factory.SelectorFactory;

import java.nio.channels.Selector;

public interface Connector
{
    String getInfo();

    Container getContainer();

    void setContainer(Container container);

    SelectorFactory getSelectorFactory();

    void setSelectorFactory(SelectorFactory selectorFactory);



}
