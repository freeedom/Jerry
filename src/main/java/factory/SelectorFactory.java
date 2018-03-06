package factory;

import java.io.IOException;
import java.nio.channels.Selector;

public interface SelectorFactory
{
    public Selector createSelector(int port) throws IOException;
}
