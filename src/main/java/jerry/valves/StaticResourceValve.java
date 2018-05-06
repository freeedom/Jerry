package jerry.valves;

import jerry.*;

import java.io.*;

public class StaticResourceValve implements Valve
{
    private Container contextContainer;

    public StaticResourceValve(Container contextContainer)
    {
        this.contextContainer = contextContainer;
    }

    @Override
    public String getInfo()
    {
        return "static resource valve";
    }

    @Override
    public void invoke(HttpRequest httpRequest, HttpResponse httpResponse, ValveSupport valveSupport)
    {
        Loader loader = contextContainer.getLoader();
        String url = httpRequest.getRequestURI();
        int index = url.indexOf(httpRequest.getContextPath());
        String streamUrl = "resources" + url.substring(index + httpRequest.getContextPath().length());
        InputStream stream = loader.getClassLoader().getResourceAsStream(streamUrl);
        if (stream != null) {
            OutputStream outputStream = null;
            try {
                outputStream = httpResponse.getOutputStream();
                readFromStreamAndWriteToOutputStream(stream, outputStream);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            valveSupport.invokeNext(httpRequest, httpResponse);
        }
    }

    private void readFromStreamAndWriteToOutputStream(InputStream inputStream, OutputStream outputStream)
    {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        byte[] bytes = new byte[1024];
        int len = -1;
        try {
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            bos.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
