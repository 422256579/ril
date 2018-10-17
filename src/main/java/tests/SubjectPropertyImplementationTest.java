package tests;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import ril.Object;
import ril.SubjectProperty;
import ril.facotry.API_Factory;
import ril.facotry.Export_Factory;
import ril.implementation.SubjectPropertyImplementation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static ril.facotry.Sort_Factory.sortObject_Importance;

public class SubjectPropertyImplementationTest {


    /**
     * This test checks whether any error occurs during retrieve objects from Wikidata.
     */
    @Test
    public void grabObjectsWIKI() {
        ArrayList<Object> objs = new ArrayList<>();
        API_Factory.scrapePositive_Objects_Wikidata("Q17714","P166",objs);
    }

    /**
     * This test checks the process grabbing labels on Wikidata according to their ID.
     */
    @Test
    public void testLGetLabelByID_WIKI(){
        String id = "Q1994";
        String str = API_Factory.scrapeLabelByID_Wikidata(id);
        assertTrue(str.equals("2011"));
    }

    /**
     * This test checks whether we can retrieve the number of views smoothly.
     */
    @Test
    public void testViews(){
        int num = API_Factory.grabViewers("Stephen_Hawking");
        System.out.println(num);
    }

    /**
     * This test checks whether we can retrieve the objects for the fixed @subject and @property using Wikidata.
     */
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

    /**
     * This test checks whether we can run the whole program for the fixed @subject and @property smoothly, when there are no local file, namely all the data must be scraped online.
     */
    public void DOALL_Online(SubjectPropertyImplementation sp) {

        SubjectProperty.API_Occurrence api = SubjectProperty.API_Occurrence.Bing;
        long startTime = System.nanoTime();

        sp.grabObjects();
        long time1 = System.nanoTime();
        long duration1 = (time1 - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabObjects(Wikidata):  " + duration1 + " ms = " + (duration1 / 60000) + " min");
        assertTrue(sp.getObjects() != null);

        sp.grabOccurrence(api);
        long time2 = System.nanoTime();
        long duration2 = (time2 - time1) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabOccurrence(" + api + "):  " + duration2 + " ms = " + (duration2 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getOccurrence(api) != -1);

        sp.grabCo_Occurrence(api);
        long time3 = System.nanoTime();
        long duration3 = (time3 - time2) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabCo_Occurrence(" + api + "):  " + duration3 + " ms = " + (duration3 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getCo_Occurrence(api) != -1);

        sp.calcCo_Occurrence_Coefficient(api);
        long time4 = System.nanoTime();
        long duration4 = (time4 - time3) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for calcCo_occurrence_Coeff:  " + duration4 + " ms = " + (duration4 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getCo_occur_coeff(api) != -1);

        System.out.println("Work for " + api + " is finished.");

        api = SubjectProperty.API_Occurrence.Wikipedia;
        sp.grabOccurrence(api);
        long time5 = System.nanoTime();
        long duration5 = (time5 - time4) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabOccurrence(" + api + "):  " + duration5 + " ms = " + (duration5 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getOccurrence(api) != -1);

        sp.grabCo_Occurrence(api);
        long time6 = System.nanoTime();
        long duration6 = (time6 - time5) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for grabCo_Occurrence(" + api + "):  " + duration6 + " ms = " + (duration6 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getCo_Occurrence(api) != -1);

        sp.calcCo_Occurrence_Coefficient(api);
        long time7 = System.nanoTime();
        long duration7 = (time7 - time6) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Time for calcCo_occurrence_Coeff:  " + duration7 + " ms = " + (duration7 / 60000) + " min");
        assertTrue(sp.getObjects().get(0).getCo_occur_coeff(api) != -1);

        long endTime = System.nanoTime();
        long duration_end = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("Total Time:  " + duration_end + " ms = " + (duration_end / 60000) + " min");
        System.out.println("Size of valid objects list of SUBJECT " + sp.getSubject_id() + "(" + sp.getSubject_label() + ")" +"  And PROPERTY " + sp.getProperty_id() + "(" + sp.getProperty_label() + ")" + "  : " + sp.getObjects().size());
    }


    @Test
    public void test_Do_All_Online_Single() {
        SubjectPropertyImplementation sp = new SubjectPropertyImplementation("Q17714","P166"); // dumpling, material used
        this.DOALL_Online(sp);
        Export_Factory.exportExcel(sp);
    }

    @Test
    public void test_fetch_SearchResult_Wikipedia(){
        String sub = "Stephen Hawking";
        String obj = "Nobel Prize";
        int num = API_Factory.scrapeCo_occurrence_Wikipedia(sub,obj);
        System.out.println(num);
        assertTrue(num != -1);
        int num_subj = API_Factory.scrapeOccurrence_Wikipedia(sub);
        System.out.println(num_subj);
        assertTrue(num_subj != -1);
        int num_obj = API_Factory.scrapeOccurrence_Wikipedia(obj);
        System.out.println(num_obj);
        assertTrue(num_obj != -1);
    }

    @Test
    public void testOnline_and_print(){
       SubjectPropertyImplementation sp1 = new SubjectPropertyImplementation("Q17714","P166"); //Stephen Hawking, awards received
        this.DOALL_Online(sp1);
        Export_Factory.exportExcel(sp1);

        SubjectPropertyImplementation sp2 = new SubjectPropertyImplementation("Q134798","P166"); //Haruki Murakami (村上春树), awards received
        this.DOALL_Online(sp2);
        Export_Factory.exportExcel(sp2);

        SubjectPropertyImplementation sp3 = new SubjectPropertyImplementation("Q1854639","P186"); // dumpling, material used
        this.DOALL_Online(sp3);
        Export_Factory.exportExcel(sp3);

        SubjectPropertyImplementation sp4 = new SubjectPropertyImplementation("Q700758","P463"); // Saarland University, member of
        this.DOALL_Online(sp4);
        Export_Factory.exportExcel(sp4);

        SubjectPropertyImplementation sp5 = new SubjectPropertyImplementation("Q35332","P166"); // Brad Pitt, awards receoved
        this.DOALL_Online(sp5);
        Export_Factory.exportExcel(sp5);

        SubjectPropertyImplementation sp6 = new SubjectPropertyImplementation("Q22686","P166"); // Donald Trump, awards receoved
        this.DOALL_Online(sp6);
        Export_Factory.exportExcel(sp6);

        SubjectPropertyImplementation sp7 = new SubjectPropertyImplementation("Q9202","P186"); // Statue of Liberty, material used
        this.DOALL_Online(sp7);
        Export_Factory.exportExcel(sp7);

        SubjectPropertyImplementation sp8 = new SubjectPropertyImplementation("Q177","P527"); // Pizza, has part
        this.DOALL_Online(sp8);
        Export_Factory.exportExcel(sp8);
    }

    @Test
    public void export_excel(){
        try {
            HSSFWorkbook excel = new HSSFWorkbook();
            HSSFSheet hssfSheet = excel.createSheet("mytest");
            HSSFRow row1 = hssfSheet.createRow(0);
            //First row
            row1.createCell(0).setCellValue("Objects");
            FileOutputStream fout = new FileOutputStream("D:\\RIL_Excel\\test.xls" );
            excel.write(fout);
            fout.close();
            System.out.println(hssfSheet.getSheetName() + " created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void grab_Occurrence_Bing_test(){
        String obj = "Order of the Garter";
        int num = API_Factory.scrapeOccurrence_Bing(obj);
        System.out.println(num);
    }

    @Test
    public void grab_Co_Occurrence_Bing_test(){
        String obj = "Order of the Garter";
        String subj = "Stephen Hawking";
        int num = API_Factory.scrapeCo_occurrence_Bing(subj,obj);
        System.out.println(num);

    }

    @Test
    public void simple_Test(){
        int num = API_Factory.scrapeCo_occurrence_Bing("Stephen Hawking", "Nobel Prize in Physics");
        System.out.println(num);
    }

    @Test
    public void sort_Test(){
        ArrayList<Object> objects = new ArrayList<>();
        Object obj1 = new Object("sub","prop","ID1","label1");
        obj1.setImportance(5);
        Object obj2 = new Object("sub","prop","ID2","label2");
        obj2.setImportance(100);
        objects.add(obj1);
        objects.add(obj2);
        objects = sortObject_Importance(objects);
        assert(objects.get(0).getImportance() == 100);


    }


}