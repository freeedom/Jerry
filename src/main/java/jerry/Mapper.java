package jerry;

public interface Mapper
{
    String getInfo();

    Container getContainer();

    void setContainer(Container container);

    Container map(HttpRequest request);
}
