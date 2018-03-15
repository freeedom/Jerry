package jerry;

public interface Loader
{
    String getInfo();

    ClassLoader getClassLoader();

    Container getContainer();

    void setContainer(Container container);

    void addRepository(String repository);

    String[] getRepositories();
}
