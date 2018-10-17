
package ril.facotry;


import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import ril.Object;
import ril.SubjectProperty;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class API_Factory {

    static final int ERROR_MAX = 10;

    static String subscriptionKey = "2faf6a70e52a46318ec658b2a14c891c";

    /**
     * This method retrieve the object-list for the fixed @subject(ID-format) and @property(ID-format).
     * @param subject
     * @param property
     * @return The ID-list, which is the result of our search for the fixed @subject and @property.
     */
	public static void scrapePositive_Objects_Wikidata(String subject, String property, List<Object> objects) {
        SPARQLRepository sparqlRepository = new SPARQLRepository("https://query.wikidata.org/sparql");
        sparqlRepository.initialize();
        RepositoryConnection sparqlConnection = sparqlRepository.getConnection();
        String query = "";

        query = "select ?x\r\n" +
                "where{\r\n" +
                " wd:" + subject + " wdt:" + property +" ?x\r\n" +
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }}";
        TupleQuery tupleQuery = sparqlConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);
        ArrayList<String> positive_objects = new ArrayList<>();
        for (BindingSet bs : QueryResults.asList(tupleQuery.evaluate())) {
            String[] strs = bs.getValue("x").stringValue().split("/");
            positive_objects.add(strs[strs.length-1]);
        }
        for(Object obj : objects){
            if(positive_objects.contains(obj.getObject_ID())){
                obj.setPositive_obj(true);
            }
        }
    }

    /**
     * This method search the label of a fixed ID string.
     * On the one hand, we use it to convert the ID-list (result of above method) to Objects' name-list.
     * e.g. For the ID "Q7191" we return "Nobel Price".
     * On the other hand, we use this method the get the name of @subject.
     * @param id The entity ID.
     * @return The label (or normal name) of an entity with this @id on Wikidata.
     */
    public static String scrapeLabelByID_Wikidata(String id){
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
     * This method scrape all possible objects, for which there are some entities which not only in the same superclass as @subject_ID but also has the triple relation <entity,@property_ID,object>
     * @param subject_ID
     * @param property_ID
     */
	public static ArrayList<Object> scrapeObjects_IDs_Wiki(String subject_ID, String property_ID){
	    // We try the case finding the subjects in the same superclass at frist.
        // If the number is too large and running overtime, then we omit the additional feature.

        ArrayList<String> superclass = new ArrayList<>();
        scrape_Object_IDs_given_SubjectAndProperty(subject_ID,"P31",superclass);
        scrape_Object_IDs_given_SubjectAndProperty(subject_ID,"P279",superclass);
        String query = "";
        query = "SELECT  ?y (count(?y) as ?cnt)\n" +
                "WHERE{\n" +
                "      ?x wdt:" + property_ID +  "   ?y.\n";
        if(superclass.size() > 0) {
            query = query + "    {?x wdt:P31   wd:" + superclass.get(0) + "} UNION {?x wdt:P279 wd:" + superclass.get(0) + " }";
            if(superclass.size()>1) {
                for (int i = 1; i < superclass.size(); i++) {
                    query = query + "    UNION {?x wdt:P31   wd:" + superclass.get(i) + "} UNION {?x wdt:P279 wd:" + superclass.get(i) + " }";
                }
            }
            query = query + ".\n";
        }
        query = query + "}\n" +
                "GROUP BY ?y\n" +
                "ORDER BY DESC(?cnt)";

        for(int error_count = 0; error_count < ERROR_MAX; error_count++){
            System.out.println("ERROR TIME: " + error_count);
            ArrayList<Object> objs = grabObjects(subject_ID,property_ID,query);
            if(objs == null){
                continue;
            }else{
                return objs;
            }
        }
        System.exit(-1);
        return null;
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
		catch(Exception e){ v = "0";}
		return Integer.parseInt(v);
	}

    /**
     * This method is used to scrape number of the co-occurrence of @subject_label and @object_label when querying on Bing.
     * @param subject_label the label of subject
     * @param object_label the label of object
     * @return the number of co-occurrence
     */
    public static int scrapeCo_occurrence_Bing(String subject_label, String object_label) {
        try {
            String api_URL = "https://api.cognitive.microsoft.com/bingcustomsearch/v7.0/search?q=\""+ URLEncoder.encode(subject_label,"UTF-8") +  "\"+\"" + URLEncoder.encode(object_label,"UTF-8") + "\"&customconfig=4be451c3-f15d-4be3-bc0c-36e9bb8ab0a0&mkt=de-DE";
            URL url = new URL(api_URL);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);
            connection.setRequestProperty("retry", "3");
            connection.setRequestProperty("retry-delay", "2");

            // receive JSON body
            InputStream stream = connection.getInputStream();

            String response = new Scanner(stream).useDelimiter("\\A").next();
            // construct result object for return
            SearchResults results = new SearchResults(new HashMap<String, String>(), response);

            stream.close();
            String str = results.jsonResponse;
            int num = API_Factory.cutNumber(str,"\"totalEstimatedMatches\": ",",");
            return num;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            System.exit(1);
            return -1;
        }
    }

    /**
     * This method is used to scrape number of the co-occurrence of object when querying on Bing.
     * @param label the label of object
     * @return the number of occurrence
     */
    public static int scrapeOccurrence_Bing(String label) {
        try {
            String api_URL = "https://api.cognitive.microsoft.com/bingcustomsearch/v7.0/search?q=\""+ URLEncoder.encode(label,"UTF-8") + "\"&customconfig=4be451c3-f15d-4be3-bc0c-36e9bb8ab0a0&mkt=de-DE";
            URL url = new URL(api_URL);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);
            connection.setRequestProperty("retry", "3");
            connection.setRequestProperty("retry-delay", "2");

            // receive JSON body
            InputStream stream = connection.getInputStream();

            String response = new Scanner(stream).useDelimiter("\\A").next();
            // construct result object for return
            SearchResults results = new SearchResults(new HashMap<String, String>(), response);

            stream.close();
            String str = results.jsonResponse;
            int num = API_Factory.cutNumber(str,"\"totalEstimatedMatches\": ",",");
            return num;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            System.exit(1);
            return -1;
        }
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

    public static int scrapeOccurrence(String label, SubjectProperty.API_Occurrence api){
        if(api == SubjectProperty.API_Occurrence.Bing){
            return scrapeOccurrence_Bing(label);
        }else{
            return scrapeOccurrence_Wikipedia(label);
        }
    }

    public static int scrapeCo_occurrence(String subject_label, String object_label, SubjectProperty.API_Occurrence api){
        if(api == SubjectProperty.API_Occurrence.Bing){
            return scrapeCo_occurrence_Bing(subject_label,object_label);
        }else{
            return scrapeCo_occurrence_Wikipedia(subject_label,object_label);
        }
    }


    private static int cutNumber(String str, String prefix, String suffix){
        String[] strs_1 = str.split(prefix);
        if(strs_1.length == 1){
            return 0;
        }
        String[] strs_2 = strs_1[1].split(suffix);
        int num = Integer.parseInt(strs_2[0]);
        return num;
    }

    //P31 for instance of
    //P279 for subclass of
    public static void scrape_Object_IDs_given_SubjectAndProperty(String subject_ID, String property_ID, ArrayList<String> object_IDs_list){
        SPARQLRepository sparqlRepository = new SPARQLRepository("https://query.wikidata.org/sparql");
        sparqlRepository.initialize();
        RepositoryConnection sparqlConnection = sparqlRepository.getConnection();
        String query = "";
        query = "SELECT ?x\n" +
                "WHERE \n" +
                "{\n" +
                "  wd:" + subject_ID +" wdt:" + property_ID + " ?x.\n" +
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }\n" +
                "}";
        TupleQuery tupleQuery = sparqlConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);

        for (BindingSet bs : QueryResults.asList(tupleQuery.evaluate())) {
            String[] strs = bs.getValue("x").stringValue().split("/");
            String str = strs[strs.length-1];
            if(!object_IDs_list.contains(str)){
                object_IDs_list.add(str);
            }
        }
    }

    public static ArrayList<String> scrape_Subject_IDs_given_PropertyAndObject(String property_ID, String object_ID, ArrayList<String> subject_IDs_list){
        SPARQLRepository sparqlRepository = new SPARQLRepository("https://query.wikidata.org/sparql");
        sparqlRepository.initialize();
        RepositoryConnection sparqlConnection = sparqlRepository.getConnection();
        String query = "";
        query = "SELECT ?x\n" +
                "WHERE \n" +
                "{\n" +
                "  ?x  wdt:" + property_ID + " wd:" + object_ID + ".\n" +
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }\n" +
                "}";
        TupleQuery tupleQuery = sparqlConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);

        for (BindingSet bs : QueryResults.asList(tupleQuery.evaluate())) {
            String[] strs = bs.getValue("x").stringValue().split("/");
            String str = strs[strs.length-1];
            if(!subject_IDs_list.contains(str)){
                subject_IDs_list.add(str);
            }
        }
        return subject_IDs_list;
    }

    public static ArrayList<Object> scrape_Object_IDs_given_property(String subject_ID, String property_ID, ArrayList<Object> objects){
        SPARQLRepository sparqlRepository = new SPARQLRepository("https://query.wikidata.org/sparql");
        sparqlRepository.initialize();
        RepositoryConnection sparqlConnection = sparqlRepository.getConnection();
        String query = "";
        query = "SELECT DISTINCT ?y\n" +
                "WHERE \n" +
                "{\n" +
                "  ?x  wdt:" + property_ID + " ?y" + ".\n" +
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }\n" +
                "}\n";
        TupleQuery tupleQuery = sparqlConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);

        for (BindingSet bs : QueryResults.asList(tupleQuery.evaluate())) {
            String[] strs = bs.getValue("y").stringValue().split("/");
            String str = strs[strs.length-1];
            String obj_label = API_Factory.scrapeLabelByID_Wikidata(str);
            if(obj_label == null){
                continue;
            }else{
                Object obj = new Object(subject_ID,property_ID,str,obj_label);
                objects.add(obj);
            }
        }
        return objects;
    }

    // property_superclass = P31 for instance of and P279 for subclass of
    public static int scrapeCountTriple_given_Property_Object_Superclass(String property, String property_superclass, String object, String superclass){
        SPARQLRepository sparqlRepository = new SPARQLRepository("https://query.wikidata.org/sparql");
        sparqlRepository.initialize();
        RepositoryConnection sparqlConnection = sparqlRepository.getConnection();
        String query = "";
        query = "select (count(*) as ?count)\n" +
                "where{\n" +
                "  ?x wdt:" + property + " wd:"+ object +".\n" +
                "  ?x wdt:" + property_superclass + " wd:"+ superclass +".\n" +
                "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }\n" +
                "}";
        TupleQuery tupleQuery = sparqlConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);

        int num = -1;
        try {
            BindingSet bs = QueryResults.asList(tupleQuery.evaluate()).get(0);
            String str = bs.getValue("count").stringValue();
            num = Integer.parseInt(str);
            System.out.println("property: " + property + "  property_superclass: " + property_superclass + "   superclass: " + superclass + "   object: " + object + "  number: " + num);
        } catch(Exception e){
            System.out.println("property: " + property + "  property_superclass: " + property_superclass + "   superclass: " + superclass + "   object: " + object);
            e.printStackTrace();
            System.exit(-1);
        }
        return num;
    }

    public static void grabCountTriple(String subject_ID, String property_ID, ArrayList<Object> objects){
        //Firstly, grab all superclass with property "instance of" (P31)
        ArrayList<String> superclass_ids_P31 = new ArrayList<>();
        API_Factory.scrape_Object_IDs_given_SubjectAndProperty(subject_ID, "P31", superclass_ids_P31);

        //Secondly. grab all superclass with property "subclass of" (P279)
        ArrayList<String> superclass_ids_P279 = new ArrayList<>();
        API_Factory.scrape_Object_IDs_given_SubjectAndProperty(subject_ID, "P279", superclass_ids_P279);

        //Finally, count the number of countTriple for each possible objects.
        for(int i=0; i<objects.size();i++){
            int num = 0;
            for(String superclass : superclass_ids_P31){
                num = num + scrapeCountTriple_given_Property_Object_Superclass(property_ID,"P31",objects.get(i).getObject_ID(),superclass);
            }
            for(String superclass : superclass_ids_P279){
                num = num + scrapeCountTriple_given_Property_Object_Superclass(property_ID,"P279",objects.get(i).getObject_ID(),superclass);
            }
            objects.get(i).setCount_triple(num);
        }
        System.out.println("This result is check by type: subject " + subject_ID + " property " + property_ID);
    }

    public static ArrayList<Object> grabObjects(String subject_ID, String property_ID, String query){
        SPARQLRepository sparqlRepository = new SPARQLRepository("https://query.wikidata.org/sparql");
        sparqlRepository.initialize();
        RepositoryConnection sparqlConnection = sparqlRepository.getConnection();

        TupleQuery tupleQuery = sparqlConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);

        ArrayList<Object> objects = new ArrayList<>();

        try {
            for (BindingSet bs : QueryResults.asList(tupleQuery.evaluate())) {
                String[] strs = bs.getValue("y").stringValue().split("/");
                String object_ID = strs[strs.length - 1];
                String str = bs.getValue("cnt").stringValue();
                int countTriple = Integer.parseInt(str);
                String object_label = scrapeLabelByID_Wikidata(object_ID);
                if (object_label == null) {
                    continue;
                } else {
                    Object obj = new Object(subject_ID, property_ID, object_ID, object_label);
                    obj.setCount_triple(countTriple);
                    objects.add(obj);
                }
            }
            return objects;
        }catch(Exception e){
            return null;
        }
    }
}