package jerry;

import javax.servlet.ServletResponse;
import java.net.Socket;

public interface Response extends ServletResponse
{
    Socket getSocket();
}
