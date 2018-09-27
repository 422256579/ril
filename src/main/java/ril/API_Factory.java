
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
import java.util.List;
import java.util.Scanner;

public class API_Factory {
	
	static String subscriptionKey = "2faf6a70e52a46318ec658b2a14c891c";
	static String customConfigId = "3701208300";  
	static String host = "https://api.cognitive.microsoft.com";
	static String path = "/bingcustomsearch/v7.0/search";

	static int resultcount = 100;

    /**
     * This method scrape the occurrence of object (or co-occurrence of subject and object, depending on the @searchQuery).
     * @param searchQuery The string of name of object / Or the string of name of subject + " " + object. Here the subject is the label of ID on Wikidata rather the ID itself.
     * @return The occurrence or co-occurrence depending @searchQuery.
     * @throws IOException
     */
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

    /**
     * This method retrieve the object-list for the fixed @subject(ID-format) and @property(ID-format).
     * @param subject
     * @param property
     * @return The ID-list, which is the result of our search for the fixed @subject and @property.
     */
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

    /**
     * This method search the label of the ID string.
     * On the one hand, we use it to convert the ID-list (result of above method) to Objects' name-list.
     * e.g. For the ID "Q7191" we return "Nobel Price".
     * On the other hand, we use this method the get the name of @subject.
     * @param id The entity ID.
     * @return The label (or normal name) of an entity with this @id on Wikidata.
     */
    public static String scrapeLabelByID_WIKI(String id){
		try {
			SPARQLRepository sparqlRepository = new SPARQLRepository("https://query.wikidata.org/sparql");
			sparqlRepository.initialize();
			RepositoryConnection sparqlConnection = sparqlRepository.getConnection();
			String query = "";
			query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
					"PREFIX wd: <http://www.wikidata.org/entity/>\n" +
					"select  *\n" +
					"where { \n" +
					"wd:" + id + " rdfs:label ?label .\n" +
					"FILTER (langMatches( lang(?label), \"EN\" ) )\n" +
					"} \n" +
					"LIMIT 1";
			TupleQuery tupleQuery = sparqlConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);
			List<BindingSet> bss = QueryResults.asList(tupleQuery.evaluate());
			if (bss.size() == 0) {
				// this case for no label defined. e.g. Q45608
				return null;
			} else {
				BindingSet bs = bss.get(0);
				return bs.getValue("label").stringValue();
			}
		}
		catch(Exception e){
			System.out.println("Error when ID:   " + id);
			return null;
		}
	}

    /**
     * This method is used to retrieve all possible objects for the fixed @property.
     * e.g. all the awards.
     * @param property_wdt the id of the property
     * @return all objects for the fixed property.
     */
	public static ArrayList<String> scrapeObjects_IDs_Wiki(String property_wdt){
		SPARQLRepository sparqlRepository = new SPARQLRepository("https://query.wikidata.org/sparql");
		sparqlRepository.initialize();
		RepositoryConnection sparqlConnection = sparqlRepository.getConnection();
		String query = "";
		query = "SELECT ?x (count(?x) as ?count) \n" +
		"WHERE \n" +
		"{ \n" +
  "?subject wdt:" + property_wdt +  " ?x. \n" +
		"} \n" +
		"GROUP BY ?x \n" +
		"ORDER BY DESC(?count) \n" +
		"LIMIT 1000";
		TupleQuery tupleQuery = sparqlConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);

		ArrayList<String> objects_ids = new ArrayList<>();
		for (BindingSet bs : QueryResults.asList(tupleQuery.evaluate())) {
			String[] strs = bs.getValue("x").stringValue().split("/");
			objects_ids.add(strs[strs.length-1]);
		}
		return objects_ids;
	}

    /**
     * This method retrieve the number of views on Wikipedia for the fixed object. Now, we consider the number of views as inherent importance.
     * @param object the name of object with underline. e.g. "Stephen_Hawking"
     * @return the number of views.
     */
	public static int grabViewers(String object){
		String startdate="2018050100";
		String enddate="2018053100";
		String link="https://wikimedia.org/api/rest_v1/metrics/pageviews/per-article/en.wikipedia/all-access/all-agents/"+object+"/monthly/"+startdate+"/"+enddate;
		String v="";

		try{
			Scanner s=new Scanner(new URL(link).openStream(), "UTF-8").useDelimiter("\\A");
			if(s.hasNext()==true) {
				String response=s.next();
				String[] strs = response.split("\"views\":");
				String str = strs[strs.length-1];
				String[] strs_2 = str.split("}]}");
				v = strs_2[0];
			}
		}
		catch(java.io.IOException e){ v = "0";}
		return Integer.parseInt(v);
	}

	public static int scrapeCo_occurrence_Wikipedia(String subject_label, String object_label){
		String[] subject_pieces = subject_label.split(" ");
		String[] object_pieces = object_label.split(" ");
        String subject_label_new = "";
        if(subject_pieces.length>1) {
            for (int i = 0; i < subject_pieces.length -1; i++) {
				subject_label_new = subject_label_new + subject_pieces[i] + "+";
			}
            subject_label_new = subject_label_new + subject_pieces[subject_pieces.length-1];
        }else{
			subject_label_new = subject_label;
		}
        String object_label_new = "";
		if(object_pieces.length>1) {
			for (int i = 0; i < object_pieces.length -1; i++) {
				object_label_new = object_label_new + object_pieces[i] + "+";
			}
			object_label_new = object_label_new + object_pieces[object_pieces.length-1];
		}else{
			object_label_new = object_label;
		}
        try{
			URL url = new URL("https://en.wikipedia.org/w/index.php?search=%22" + subject_label_new + "%22+%22" + object_label_new + "%22&title=Special:Search&profile=default&fulltext=1&searchToken=4zplxaeirgstja7g4efz6et5t");//网址链接
			String out = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
			String[] strs = out.split("data-mw-num-results-total=\"");
            if(strs.length == 1){
                return 0;
            }
            String[] strs_1 = strs[1].split("\"",2);
			String str = strs_1[0];

			int num = Integer.parseInt(str);
			return num;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}

    public static int scrapeOccurrence_Wikipedia(String label) {
        String[] label_pieces = label.split(" ");
        String label_new = "";
        if (label_pieces.length > 1) {
            for (int i = 0; i < label_pieces.length - 1; i++) {
                label_new = label_new + label_pieces[i] + "+";
            }
            label_new = label_new + label_pieces[label_pieces.length - 1];
        } else {
            label_new = label;
        }
        try {
            URL url = new URL("https://en.wikipedia.org/w/index.php?search=%22" + label_new + "%22&title=Special:Search&profile=default&fulltext=1&searchToken=4zplxaeirgstja7g4efz6et5t");//网址链接
            String out = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
            String[] strs = out.split("data-mw-num-results-total=\"");
            if(strs.length == 1){
                return 0;
            }

            String[] strs_1 = strs[1].split("\"", 2);
            String str = strs_1[0];

            int num = Integer.parseInt(str);
            return num;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }
}


