package jerry;

public interface Mapper
{
    Container getContainer();

    void setContainer(Container container);

    Container map(Request request);
}
