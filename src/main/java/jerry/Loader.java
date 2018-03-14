package jerry;

public interface Loader
{
    ClassLoader getClassLoader();

    Container getContainer();

    void setContainer();

    void addRepository(String repository);

    String[] getRepositories();
}
