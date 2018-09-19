package tests;

import org.junit.Test;
import ril.API_Factory;
import ril.Object;
import ril.implementation.SubjectPropertyImplementation;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class SubjectPropertyImplementationTest {

    @Test
    public void grabObjects() {
        long startTime = System.nanoTime();

        SubjectPropertyImplementation sp = new SubjectPropertyImplementation("Hawking","Awards");
        String filePath = "src/main/java/resource/Awards.txt";
        sp.grabObjects(filePath);
        long time1 = System.nanoTime();
        long duration1 = (time1 - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabObjects(Local):  " + duration1 + "ms");
        assertTrue(sp.getObjects() != null);

        sp.grabNum_objs();
        long time2 = System.nanoTime();
        long duration2 = (time2 - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabNum_objs:  " + duration2 + "ms");
        assertTrue(sp.getObjects().get(0).getNum_object() != -1);

        sp.grabNum_subj_objs();
        long time3 = System.nanoTime();
        long duration3 = (time3 - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabNum_subj_objs:  " + duration3 + "ms");
        assertTrue(sp.getObjects().get(0).getNum_subj_object() != -1);

        sp.calcRank_coeff();
        long time4 = System.nanoTime();
        long duration4 = (time4 - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for calcRank_coeff:  " + duration4 + "ms");
        assertTrue(sp.getObjects().get(0).getRank_coeff() != -1);

        sp.sortObject(Object.Parameter.RANK_COEFF);
        long time5 = System.nanoTime();
        long duration5 = (time5 - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for resort:  " + duration5 + "ms");
        assertTrue(sp.getObjects().get(0).getRank_coeff() >= sp.getObjects().get(1).getRank_coeff());
        assertTrue(sp.sort_order == Object.Parameter.RANK_COEFF);

        sp.printResults(10);
        long time6 = System.nanoTime();
        long duration6 = (time6 - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for print:  " + duration6 + "ms");
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Total Time:  " + duration + "ms");
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
}