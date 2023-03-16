package scraper;
import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.Document;

public class WebScraper {

    public String prepQuery(String query)
    {
        String str[] = query.split(" ");
        String result="";
        for(int i=0;i<str.length-1;++i)
            result=str[i]+"+";
        return result+str[str.length-1];
    }
    public Document get_google_Data(String Query) throws IOException
    {
        // Make Google Request According to the Search Query.
        String url="http://google.com/search?q=";
        Document doc = Jsoup.connect(url+Query)
                .timeout(50000).ignoreHttpErrors(true)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                .get();
        System.out.println(url+Query);
        return doc;
    }
}
