
package ril;


import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
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
		if(!response.contains("\"totalEstimatedMatches\": ")){
			return  -1;
		}
		String[] strs_split_1 = response.split("\"totalEstimatedMatches\": ",2);
		String str = strs_split_1[1];
		String[] strs_split_2 = str.split(",",2);
		int count = Integer.parseInt(strs_split_2[0]);
		return count;
	}

	public static ArrayList<String> scrapeWiki(String subject, String property) {
        SPARQLRepository sparqlRepository = new SPARQLRepository("https://query.wikidata.org/sparql");
        sparqlRepository.initialize();
        RepositoryConnection sparqlConnection = sparqlRepository.getConnection();
        String query = "";

        query = "select ?x ?xLabel\r\n" +
                "where{\r\n" +
                " wd:" + subject + " wdt:" + property +" ?x\r\n" +
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }}";
        TupleQuery tupleQuery = sparqlConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);
        ArrayList<String> positive_objects = new ArrayList<>();
        for (BindingSet bs : QueryResults.asList(tupleQuery.evaluate())) {
            positive_objects.add(bs.getValue("xLabel").stringValue());
        }
        return positive_objects;
    }
}
