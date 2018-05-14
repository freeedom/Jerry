package jerry.test;
import jerry.Main;
import org.apache.log4j.Logger;


public class TestLog
{
    private static Logger logger = Logger.getLogger(TestLog.class);
    private static Logger logger2 = Logger.getLogger(Main.class);

    public static void main(String[] args)
    {
        logger.debug("this is a bug message");

        logger.info("this is info message");

        logger.error("this is error message");

        logger2.debug("this is a bug message");

        logger2.info("this is info message");

        logger2.error("this is error message");
    }
}
