package factory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class DefaultSelectorFactory implements SelectorFactory
{
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    public Selector createSelector(int port) throws IOException
    {
        if(selector==null)
        {
            synchronized (selector)
            {
                if(selector==null)
                {
                    selector=Selector.open();
                    serverSocketChannel=ServerSocketChannel.open();
                    serverSocketChannel.configureBlocking(false);
                    serverSocketChannel.bind(new InetSocketAddress(port));
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                }
            }
        }
        return selector;
    }
}
