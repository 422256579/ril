package tests;

import org.junit.Test;
import ril.API_Factory;
import ril.Object;
import ril.implementation.SubjectPropertyImplementation;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class SubjectPropertyImplementationTest {

    @Test
    public void DOALL_Local() {
        long startTime = System.nanoTime();

        SubjectPropertyImplementation sp = new SubjectPropertyImplementation("Q17714","P166");
        String filePath = "src/main/java/resource/Awards.txt";
        sp.grabObjects(filePath);
        long time1 = System.nanoTime();
        long duration1 = (time1 - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabObjects(Local):  " + duration1 + " ms = " + (duration1 / 60000) + " min");
        assertTrue(sp.getObjects() != null);

        sp.grabNum_objs();
        long time2 = System.nanoTime();
        long duration2 = (time2 - time1) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabNum_objs:  " + duration2 + " ms = " + (duration2 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getNum_object() != -1);

        sp.grabNum_subj_objs();
        long time3 = System.nanoTime();
        long duration3 = (time3 - time2) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabNum_subj_objs:  " + duration3 + " ms = " + (duration3 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getNum_subj_object() != -1);

        sp.calcRank_coeff();
        long time4 = System.nanoTime();
        long duration4 = (time4 - time3) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for calcRank_coeff:  " + duration4 + " ms = " + (duration4 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getRank_coeff() != -1);

        sp.sortObject(Object.Parameter.RANK_COEFF);
        long time5 = System.nanoTime();
        long duration5 = (time5 - time4) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for resort:  " + duration5 + " ms = " + (duration5 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getRank_coeff() >= sp.getObjects().get(1).getRank_coeff());
        assertTrue(sp.sort_order == Object.Parameter.RANK_COEFF);

        sp.printResults(10);
        long time6 = System.nanoTime();
        long duration6 = (time6 - time5) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for print:  " + duration6 + " ms = " + (duration6 / 60000) + " min");
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Total Time :  " + duration + " ms = " + (duration / 60000) + " min");
        System.out.println("Size of Valid negative objects list of SUBJECT " + sp.getSubject() + "  And PROPERTY " +  sp.getProperty() + "  : " + sp.getObjects().size());

    }

    @Test
    public void grabObjectsWIKI() {
        API_Factory.scrapeWiki("Q17714","P166");
    }

    @Test
    public void removeNegativeObjects(){
        SubjectPropertyImplementation sp = new SubjectPropertyImplementation("Q17714","P166");

        String filePath = "src/main/java/resource/Awards.txt";
        sp.grabObjects(filePath);
        ArrayList<Object> objects_negative = sp.getObjects();
        System.out.println("Size of Negative Awards :  " + objects_negative.size());
    }

    @Test
    public void testLGetLabelByID_WIKI(){
        String id = "Q1994";
        String str = API_Factory.scrapeLabelByID_WIKI(id);
        assertTrue(str.equals("2011"));
    }

    @Test
    public void testObject_IDs_WIKI(){
        String property_wdt = "P166";
        ArrayList<String> ids = API_Factory.scrapeObjects_IDs_Wiki(property_wdt);
        assertTrue(ids != null);
        assertTrue(ids.size()> 10000);
        System.out.println(ids.get(0));
    }

    @Test
    public void testObject_ID_to_Label(){
        String property_wdt = "P166";
        ArrayList<String> ids = API_Factory.scrapeObjects_IDs_Wiki(property_wdt);
        String id = ids.get(0);
        System.out.println("id:   " + id);
        String label = API_Factory.scrapeLabelByID_WIKI(id);
        System.out.println("label:   " + label);
    }

    @Test
    public void testViews(){
        int num = API_Factory.grabViewers("Stephen_Hawking");
        System.out.println(num);
    }

    @Test
    public void grabObjects_Online() {
        long startTime = System.nanoTime();

        SubjectPropertyImplementation sp = new SubjectPropertyImplementation("Q17714","P166");
        sp.grabObjects();
        long time1 = System.nanoTime();
        long duration1 = (time1 - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabObjects(Wiki):  " + duration1 + " ms = " + (duration1 / 60000) + " min");
        assertTrue(sp.getObjects() != null);

    }

    @Test
    public void DOALL_Online() {
        long startTime = System.nanoTime();

        SubjectPropertyImplementation sp = new SubjectPropertyImplementation("Q17714","P166");
        sp.grabObjects();
        long time1 = System.nanoTime();
        long duration1 = (time1 - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabObjects(Wiki):  " + duration1 + " ms = " + (duration1 / 60000) + " min");
        assertTrue(sp.getObjects() != null);

        sp.grabNum_objs();
        long time2 = System.nanoTime();
        long duration2 = (time2 - time1) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabNum_objs:  " + duration2 + " ms = " + (duration2 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getNum_object() != -1);

        sp.grabNum_subj_objs();
        long time3 = System.nanoTime();
        long duration3 = (time3 - time2) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabNum_subj_objs:  " + duration3 + " ms = " + (duration3 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getNum_subj_object() != -1);

        sp.calcRank_coeff();
        long time4 = System.nanoTime();
        long duration4 = (time4 - time3) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for calcRank_coeff:  " + duration4 + " ms = " + (duration4 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getRank_coeff() != -1);

        sp.sortObject(Object.Parameter.RANK_COEFF);
        long time5 = System.nanoTime();
        long duration5 = (time5 - time4) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for resort:  " + duration5 + " ms = " + (duration5 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getRank_coeff() >= sp.getObjects().get(1).getRank_coeff());
        assertTrue(sp.sort_order == Object.Parameter.RANK_COEFF);

        sp.printResults(10);
        long time6 = System.nanoTime();
        long duration6 = (time6 - time5) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for print:  " + duration6 + " ms = " + (duration6 / 60000) + " min");
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Total Time:  " + duration + " ms = " + (duration / 60000) + " min");
        System.out.println("Size of Valid negative objects list of SUBJECT " + sp.getSubject() + "  And PROPERTY " +  sp.getProperty() + "  : " + sp.getObjects().size());
    }
}