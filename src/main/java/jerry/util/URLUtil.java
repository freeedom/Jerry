package jerry.util;

import java.util.ArrayList;
import java.util.List;

public class URLUtil
{
    public static List<String> splitUrl(String url)
    {
        int index=-1;
        List<String> list=new ArrayList<>();
        while ((index=url.lastIndexOf('/'))!=-1)
        {

            list.add(url.replace("*",""));
            url=url.substring(0,index);
        }
        return list;
    }

    public static void main(String[] args)
    {
        System.out.println(splitUrl("/111/*"));
    }
}
