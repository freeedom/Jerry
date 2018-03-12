package jerry;

import javax.servlet.ServletRequest;
import java.net.Socket;

public interface Request extends ServletRequest
{
    void setParameter(String key,String value);

    void setProtocol(String protocol);

    void setContentLength(int contentLength);

    void setContentType(String contentType);

    Socket getSocket();
}
