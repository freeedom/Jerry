package jerry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Container
{
    String getInfo();

    String getName();

    void setName(String name);

    void addChild(Container container);

    Container getChild(String name);

    Container[] getChilds();

    void setParent(Container container);

    Container getParent();

    void invoke(HttpServletRequest request, HttpServletResponse response);

    void setLoader(Loader loader);

    Loader getLoader();

    Loader getParentLoader();
}
