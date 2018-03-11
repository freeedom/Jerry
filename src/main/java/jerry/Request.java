package jerry;

import javax.servlet.ServletRequest;

public interface Request extends ServletRequest
{
    void setParameter(String key,String value);

    void setProtocol(String protocol);
}
