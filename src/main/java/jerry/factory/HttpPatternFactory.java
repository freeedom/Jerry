package jerry.factory;

import java.util.regex.Pattern;

public interface HttpPatternFactory
{
    Pattern getRequestLinePattern();

    Pattern getParameterPattern();
}
