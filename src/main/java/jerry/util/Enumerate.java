package jerry.util;

import java.util.Enumeration;
import java.util.Iterator;

public class Enumerate<A> implements Enumeration<A>
{
    private Iterator<A> iterator;

    public Enumerate(Iterator<A> iterator)
    {
        this.iterator = iterator;
    }

    public boolean hasMoreElements()
    {
        return iterator.hasNext();
    }

    public A nextElement()
    {
        return iterator.next();
    }
}
