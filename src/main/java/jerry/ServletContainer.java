package jerry;

public interface ServletContainer extends Container
{
    String getServletClass();

    void setServletClass(String servletClass);

    void load() throws IllegalAccessException, InstantiationException;
}
