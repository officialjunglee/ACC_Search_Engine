package stropr;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import boyermoore.BoyerMoore;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringOperations {
    public String dnsExtractor(String val,boolean exclude)
    {
        // Extract Domain Name from Link using Regex Matcher and basic String Matching
        Pattern pattern = Pattern.compile("^(?:https?:\\\\/\\\\/)?(?:[^@\\\\n]+@)?(?:www\\\\.)?([^:\\\\/\\\\n?]+)");
        Matcher matcher = pattern.matcher(val);
        if (matcher.find())
        {
            val=matcher.group(1);
        }
        if(exclude)
        {
            int d = val.indexOf("https");
            val = val.substring(d);
        }
        int dot = val.indexOf("/");
        int slash = val.indexOf("/", dot+1);
        dot = val.indexOf("/",slash+1);
        val = val.substring(slash+1,dot);
        return val;
    }


    public TreeMap<String, String> keywordCounter(String temp, String pattern)  throws IOException
    {
        String link;
        String Blob;
        TreeMap<String,String> data = new TreeMap<String,String>();
        // Scrape Data from the URL of the given result
        Document doc = Jsoup.connect(temp)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 13_2_1 ) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36")
                .ignoreHttpErrors(true).timeout(10000).ignoreContentType(true).get();
        link=temp;
        // Convert HTML to Text
        String data_Res=doc.text();
        // Extract Domain Names from the Link
        String regex = "(?<=://)([\\w-]+\\.)+[\\w-]+(?<=/?)";
        String domain=null;
        String url = "https://www.example.com/path/to/page.html";
        Pattern pt = Pattern.compile(regex);
        Matcher matcher = pt.matcher(temp);
        if (matcher.find()) {
            domain = matcher.group(0);
        }


        // Fetch first 200 characters from the Results
        if(data_Res.length()>200)
        {
            Blob=doc.text().substring(0,200)+"...";
        }
        else
        {
            Blob=doc.text().substring(0,doc.text().length());
        }
        // Find the number of Occurences in the Text using Boyer Moore Algorithm.
        String Pattern=pattern;
        BoyerMoore boyermoore1 = new BoyerMoore(Pattern);
        int count=0;
        while(true)
        {
            if(boyermoore1.search(data_Res)>=data_Res.length())
            {
                break;
            }
            else
            {
                int offset=boyermoore1.search(data_Res);
                if(offset+pattern.length()>=data_Res.length())
                {
                    count++;
                    break;
                }
                else
                {
                    data_Res=data_Res.substring(offset+pattern.length());
                }
                count++;
            }
        }
        // Put the Data in a TreeMap and Return
        data.put("Blob",Blob);
        data.put("Link",link);
        data.put("Domain", domain);
        data.put("occurences",Integer.toString(count));
        return data;
    }
    public TreeMap<String, String[]> frequency(Set<String> set, String html)
    {
        // Create a new HashTable
        TreeMap<Integer,String[]> HashTable = new TreeMap<Integer,String[]>();
        // Get the number of Times the URL has occured
        for(String value:set)
        {
            int freq=html.replace(value, value + "x").length() - html.length();
            if(HashTable.get(freq)==null)
            {
                String[] arr= {value};
                HashTable.put(freq,arr);
            }
            else
            {
                String[] arr=HashTable.get(freq);
                List<String> arrlist
                        = new ArrayList<String>(
                        Arrays.asList(arr));
                arrlist.add(value);
                arr = arrlist.toArray(arr);
                HashTable.put(freq, arr);
            }
        }
        // Store the occurences in a HashTable
        TreeMap<String,String[]> final_data = new TreeMap<String,String[]>();
        Integer most_key=HashTable.lastKey();
        String[] most_value=HashTable.get(most_key);
        String[] most_keys= {most_key+""};
        //Returned the Most Occuring Domains
        final_data.put("most_domain", most_value);
        final_data.put("most_times", most_keys);
        Integer least_key=HashTable.firstKey();
        String[] least_value=HashTable.get(least_key);
        String[] least_keys= {least_key+""};
        //Returned the Least Occuring Domains
        final_data.put("least_domain", least_value);
        final_data.put("least_times", least_keys);
        return final_data;
    }
}
