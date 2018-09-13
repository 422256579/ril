
package ril;


import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class API_Factory {
	
	static String subscriptionKey = "2faf6a70e52a46318ec658b2a14c891c";
	static String customConfigId = "3701208300";  
	static String host = "https://api.cognitive.microsoft.com";
	static String path = "/bingcustomsearch/v7.0/search";

	static int resultcount = 100;

	public static int scrapeBing(String searchQuery) throws IOException {
		URL url = new URL(host + path + "?q=" +  URLEncoder.encode(searchQuery,
				"UTF-8")+ "&CustomConfig=" + customConfigId +"&count=" + resultcount
				+"&mkt=en-US&safesearch=Off&offset=0&responseFilter=webpages%2Cnews" );
		HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
		connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);
		connection.setRequestProperty("retry", "3");
		connection.setRequestProperty("retry-delay", "2");

		// receive JSON body
		InputStream stream = connection.getInputStream();
		String response = new Scanner(stream).useDelimiter("\\A").next();
		String[] strs_split_1 = response.split("\"totalEstimatedMatches\": ",2);
		String str = strs_split_1[1];
		String[] strs_split_2 = str.split(",",2);
		int count = Integer.parseInt(strs_split_2[0]);
		return count;
	}
}
