package jerry.parse;

import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class ParseXml
{
    private static Logger logger = Logger.getLogger(ParseXml.class);
    public static WebAppBean parse(String fileName)
    {
        WebAppBean webAppBean=null;
        try {
            makeNewTmpXml(fileName);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName + ".tmp");
            Node webApp = document.getChildNodes().item(0);
            NodeList contexts = webApp.getChildNodes();
            webAppBean = new WebAppBean();
            logger.debug("----------------server.xml---------------");
            for (int i = 0; i < contexts.getLength(); i++) {
                ContextBean bean=parseContext(contexts.item(i));
                webAppBean.addContextBean(bean);
                logger.debug(bean.toString());
            }
            rmTmpXml(fileName);
            return webAppBean;
        }
        catch (ParserConfigurationException e) {
            logger.error("解析错误",e);
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            logger.error("配置文件加载错误",e);
        }
        return webAppBean;
    }

    private static ContextBean parseContext(Node contextNode)
    {
        ContextBean contextBean = new ContextBean();
        NamedNodeMap attributeMap = contextNode.getAttributes();
        Node name = attributeMap.getNamedItem("name");
        contextBean.setName(name.getNodeValue());
        Node port = attributeMap.getNamedItem("port");
        contextBean.setPort(Integer.parseInt(port.getNodeValue()));
        Node debug = attributeMap.getNamedItem("debug");
        contextBean.setDebug(debug.getNodeValue());
        NodeList childList = contextNode.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            Node node = childList.item(i);
            if (node.getNodeName() == "webroot") {
                contextBean.setWebroot(node.getTextContent());
            }
            else if (node.getNodeName() == "servlet") {
                NodeList servletNodeList = node.getChildNodes();
                String servletName = servletNodeList.item(0).getTextContent();
                String servletClass = servletNodeList.item(1).getTextContent();
                contextBean.setServletNameClass(servletName, servletClass);
            }
            else if (node.getNodeName() == "servlet-mapping") {
                NodeList servletNodeList = node.getChildNodes();
                String servletName = servletNodeList.item(0).getTextContent();
                String servletUrl = servletNodeList.item(1).getTextContent();
                contextBean.setServletNameUrl(servletName, servletUrl);
            }
        }
        return contextBean;
    }

    private static void makeNewTmpXml(String fileName)
    {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName + ".tmp"));
            String s = null;
            while ((s = br.readLine()) != null) {
                bw.write(s.trim());
            }
            br.close();
            bw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void rmTmpXml(String filename)
    {
        File f = new File(filename + ".tmp");
        f.delete();
    }

//    public static void main(String[] args)
//    {
//        WebAppBean webAppBean= parse("webroot/server.xml");
//    }
}

