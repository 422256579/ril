package tests;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import ril.Object;
import ril.SubjectProperty;
import ril.facotry.API_Factory;
import ril.facotry.Export_Factory;
import ril.facotry.Import_Factory;
import ril.facotry.Sort_Factory;
import ril.implementation.SubjectPropertyImplementation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static ril.facotry.API_Factory.*;
import static ril.facotry.Import_Factory.*;
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

        sp.computeCo_Occurrence_Coefficient(api);
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

        sp.computeCo_Occurrence_Coefficient(api);
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
        List<SubjectProperty> sps = new ArrayList<>();
        sps.add(sp);
        Export_Factory.exportExcel(sps);
    }


    @Test
    public void testOnline_and_print(){
       SubjectPropertyImplementation sp1 = new SubjectPropertyImplementation("Q17714","P166"); //Stephen Hawking, awards received
        SubjectPropertyImplementation sp2 = new SubjectPropertyImplementation("Q134798","P166"); //Haruki Murakami (村上春树), awards received
        SubjectPropertyImplementation sp3 = new SubjectPropertyImplementation("Q1854639","P186"); // dumpling, material used
        SubjectPropertyImplementation sp4 = new SubjectPropertyImplementation("Q700758","P463"); // Saarland University, member of
        SubjectPropertyImplementation sp5 = new SubjectPropertyImplementation("Q35332","P166"); // Brad Pitt, awards receoved
        SubjectPropertyImplementation sp6 = new SubjectPropertyImplementation("Q22686","P166"); // Donald Trump, awards receoved
        SubjectPropertyImplementation sp7 = new SubjectPropertyImplementation("Q9202","P186"); // Statue of Liberty, material used
        SubjectPropertyImplementation sp8 = new SubjectPropertyImplementation("Q177","P527"); // Pizza, has part
        this.DOALL_Online(sp1);
        this.DOALL_Online(sp2);
        this.DOALL_Online(sp3);
        this.DOALL_Online(sp4);
        this.DOALL_Online(sp5);
        this.DOALL_Online(sp6);
        this.DOALL_Online(sp7);
        this.DOALL_Online(sp8);

        List<SubjectProperty> sps = new ArrayList<>();
        sps.add(sp1);
        sps.add(sp2);
        sps.add(sp3);
        sps.add(sp4);
        sps.add(sp5);
        sps.add(sp6);
        sps.add(sp7);
        sps.add(sp8);
        Export_Factory.exportExcel(sps);
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

    public void NDCG_Single(String path){
        SubjectProperty sp = readExcel_Folder6(path).get(0);

        double NDCG_For_Importance = sp.computeNDCG(Sort_Factory.Parameter.IMPORTANCE);
        double NDCG_For_CO_OCCUR_COEFF_Wikipedia = sp.computeNDCG(Sort_Factory.Parameter.CO_OCCURRENCE_COEFFICIENT_Wikipedia);
        double NDCG_For_CO_OCCUR_COEFF_Bing = sp.computeNDCG(Sort_Factory.Parameter.CO_OCCURRENCE_COEFFICIENT_Bing);
        double NDCG_FOR_CountTriple = sp.computeNDCG(Sort_Factory.Parameter.COUNTFACTS);

        System.out.println(" For file :" + path);
        System.out.println(" Subject: " + sp.getSubject_label() + "/" + sp.getSubject_id());
        System.out.println(" Property: " + sp.getProperty_label() + "/" + sp.getProperty_id());
        System.out.println(" IDCG : " + sp.getIDCG());
        System.out.println(" NDCG for Importance : " + NDCG_For_Importance);
        System.out.println(" NDCG for Co_Occurrence_Coefficient_Wikipedia : " + NDCG_For_CO_OCCUR_COEFF_Wikipedia);
        System.out.println(" NDCG for Co_Occurrence_Coefficient_Bing : " + NDCG_For_CO_OCCUR_COEFF_Bing);
        System.out.println(" NDCG for countTriple : " + NDCG_FOR_CountTriple);
        System.out.println(" NDCG 1:1:1:1 :"  + ((NDCG_For_Importance + NDCG_For_CO_OCCUR_COEFF_Wikipedia + NDCG_For_CO_OCCUR_COEFF_Bing + NDCG_FOR_CountTriple) / 4.0));
    }

    @Test
    public void NDCG_tests(){
        String folderPath = "D:\\RIL_Excel\\6\\";
        String file1 = "Q177_P527_top100.xls";
        String file2 = "Q9202_P186_top100.xls";
        String file3 = "Q17714_P166_top100.xls";
        String file4 = "Q22686_P166_top100.xls";
        // without Simon
        String file5 = "Q35332_P166_top100.xls";
        //without Simon
        String file6 = "Q134798_P166_top100.xls";
        //without Simon
        String file7 = "Q700758_P463_top100.xls";
        // without Simon,Hiba
        String file8 = "Q1854639_P186_top100.xls";

        NDCG_Single(folderPath + file1);
        NDCG_Single(folderPath + file2);
        NDCG_Single(folderPath + file3);
        NDCG_Single(folderPath + file4);
        NDCG_Single(folderPath + file5);
        NDCG_Single(folderPath + file6);
        NDCG_Single(folderPath + file7);
        NDCG_Single(folderPath + file8);
    }

    public void Weka_Single(String path){
        SubjectProperty sp = readExcel_Folder6(path).get(0);
        Export_Factory.exportARFF(sp);
        String filePath = "D:\\RIL_Excel\\6\\" + sp.getSubject_id() + "_" + sp.getProperty_id() + ".arff";
        Import_Factory.readARFF_weka(filePath);
    }

    @Test
    public void weka_test(){
        String folderPath = "D:\\RIL_Excel\\6\\";
        String file1 = "Q177_P527_top100.xls";
        String file2 = "Q9202_P186_top100.xls";
        String file3 = "Q17714_P166_top100.xls";
        String file4 = "Q22686_P166_top100.xls";
        // without Simon
        String file5 = "Q35332_P166_top100.xls";
        //without Simon
        String file6 = "Q134798_P166_top100.xls";
        //without Simon
        String file7 = "Q700758_P463_top100.xls";
        // without Simon,Hiba
        String file8 = "Q1854639_P186_top100.xls";

        this.Weka_Single(folderPath + file1);
        this.Weka_Single(folderPath + file2);
        this.Weka_Single(folderPath + file3);
        this.Weka_Single(folderPath + file4);
        this.Weka_Single(folderPath + file5);
        this.Weka_Single(folderPath + file6);
        this.Weka_Single(folderPath + file7);
        this.Weka_Single(folderPath + file8);

    }

    @Test
    public void Fleiss_Kappa_SimpleTest() {
        String folderPath = "D:\\RIL_Excel\\test\\";
        String file1 = "test1.xls";
        System.out.println("The agreement for file " + file1 + " is: " + readExcel_Fleiss_Kappa(folderPath + file1));
    }

    @Test
    public void Fleiss_Kappa_test(){
        String folderPath = "D:\\RIL_Excel\\6\\";
        String file1 = "Q177_P527_top100.xls";
        String file2 = "Q9202_P186_top100.xls";
        String file3 = "Q17714_P166_top100.xls";
        String file4 = "Q22686_P166_top100.xls";
        // without Simon
        String file5 = "Q35332_P166_top100.xls";
        //without Simon
        String file6 = "Q134798_P166_top100.xls";
        //without Simon
        String file7 = "Q700758_P463_top100.xls";
        // without Simon,Hiba
        String file8 = "Q1854639_P186_top100.xls";

        System.out.println("The agreement for file " + file1 + " is: " + readExcel_Fleiss_Kappa(folderPath + file1));
        System.out.println("The agreement for file " + file2 + " is: " + readExcel_Fleiss_Kappa(folderPath + file2));
        System.out.println("The agreement for file " + file3 + " is: " + readExcel_Fleiss_Kappa(folderPath + file3));
        System.out.println("The agreement for file " + file4 + " is: " + readExcel_Fleiss_Kappa(folderPath + file4));
        System.out.println("The agreement for file " + file5 + " is: " + readExcel_Fleiss_Kappa(folderPath + file5));
        System.out.println("The agreement for file " + file6 + " is: " + readExcel_Fleiss_Kappa(folderPath + file6));
        System.out.println("The agreement for file " + file7 + " is: " + readExcel_Fleiss_Kappa(folderPath + file7));
        System.out.println("The agreement for file " + file8 + " is: " + readExcel_Fleiss_Kappa(folderPath + file8));
    }

    @Test
    public void wekaTest(){
        String filePath = "D:\\RIL_Excel\\6\\Q177_P527.arff";
        Import_Factory.readARFF_weka(filePath);
    }

    @Test
    public void weka_Test(){
        String folderPath = "D:\\RIL_Excel\\7\\";
        String file1 = "allFiles.xls";
        SubjectProperty sp = Import_Factory.readExcel_Folder6(folderPath + file1).get(0);
        Export_Factory.exportARFF(sp);
        Import_Factory.readARFF_weka(folderPath + "allFiles.arff");
    }

    @Test
    public void countAgreement_Test(){
        String folderPath = "D:\\RIL_Excel\\7\\";
        String file1 = "allFiles.xls";
        int[] countAgreement = Import_Factory.readExcel_countAgreement(folderPath + file1);
        System.out.println("Disagreement : " + countAgreement[0] + "  intermediate agreement : " + countAgreement[1] + "  agreement : " + countAgreement[2]);
    }

    @Test
    public void countAgreement_SimpleTest(){
        String folderPath = "D:\\RIL_Excel\\test\\";
        String file1 = "test1.xls";
        int[] countAgreement = Import_Factory.readExcel_countAgreement(folderPath + file1);
        System.out.println("Disagreement : " + countAgreement[0] + "  intermediate agreement : " + countAgreement[1] + "  agreement : " + countAgreement[2]);
    }

    @Test
    public void Weka_LinearRegression_WithSubclassSuperclass(){
        String folderPath = "D:\\RIL_Excel\\7\\";
        String file1 = folderPath + "Q177_P527_top100.xls";
        String file2 = folderPath + "Q9202_P186_top100.xls";
        String file3 = folderPath + "Q17714_P166_top100.xls";
        String file4 = folderPath + "Q22686_P166_top100.xls";

        String[] files = {file1, file2,file3,file4};
        for(int i=0; i<files.length;i++){
            SubjectProperty sp = readExcel_Folder6(files[i]).get(0);
            ArrayList<Object> objs = sp.getObjects();
            for(int j=0; j<objs.size();j++) {
                int numSub = grabNumberForSubclass(objs.get(j).getObject_ID());
                int numSuper = grabNumberForSuperclass(objs.get(j).getObject_ID());
                objs.get(j).setNumSubclass(numSub);
                objs.get(j).setNumSuperclass(numSuper);
            }

            sp.setObjects(objs);
            sp.sortObject(Sort_Factory.Parameter.NUMSUPERCLASS);
            sp.sortObject(Sort_Factory.Parameter.NUMSUBCLASS);

            Export_Factory.exportARFF(sp);
            Import_Factory.readARFF_weka("D:\\RIL_Excel\\7\\" + sp.getSubject_id() + "_" + sp.getProperty_id() + ".arff");
        }
    }

    @Test
    public void Weka_LinearRegression_WithSubclassSuperclass_Together(){
        String folderPath = "D:\\RIL_Excel\\7\\";
        String file1 = folderPath + "all.arff";
        readARFF_weka(file1);
    }

    @Test //pass
    public void rekursiv_Distance_SimpleTest(){
        System.out.println(rekursiv_GrabDistance("Q22686", "Q6294",1));

        System.out.println(grabDistance("Q22686", "Q6294"));
    }

    @Test
    public void ConvertExcelAndOutputARFF_Folder6(){
        String folderPath = "D:\\RIL_Excel\\6\\";
        String file1 = "Q177_P527_top100.xls";
        String file2 = "Q9202_P186_top100.xls";
        String file3 = "Q17714_P166_top100.xls";
        String file4 = "Q22686_P166_top100.xls";

        List<SubjectProperty> sps1 = readExcel_Folder6(folderPath + file1);
        List<SubjectProperty> sps2 = readExcel_Folder6(folderPath + file2);
        List<SubjectProperty> sps3 = readExcel_Folder6(folderPath + file3);
        List<SubjectProperty> sps4 = readExcel_Folder6(folderPath + file4);

        List<SubjectProperty> sps = new ArrayList<>();
        sps.add(sps1.get(0));
        sps.add(sps2.get(0));
        sps.add(sps3.get(0));
        sps.add(sps4.get(0));

        for(int i=0; i<sps.size();i++){
            sps.get(i).grabNumSuperclass();
            System.out.println("finish grab Number of Superclass " + i);
            sps.get(i).grabNumSubclass();
            System.out.println("finish grab Number of Subclass " + i);
            sps.get(i).grabDistance();
            System.out.println("finish grab Number of distance " + i);
            sps.get(i).grabNumSubject();
            System.out.println("finish grab Number of NumSubject " + i);
            sps.get(i).grabNumSubjectProperty();
            System.out.println("finish grab Number of NumSubjectProperty " + i);

            sps.get(i).sortObject(Sort_Factory.Parameter.NUMSUPERCLASS);
            sps.get(i).sortObject(Sort_Factory.Parameter.NUMSUBCLASS);
            sps.get(i).sortObject(Sort_Factory.Parameter.DISTANCE);
            sps.get(i).sortObject(Sort_Factory.Parameter.NUMSUBJECT);
            sps.get(i).sortObject(Sort_Factory.Parameter.NUMSUBJECTPROPERTY);

            System.out.println("finish  " + i);
            Export_Factory.exportARFF(sps.get(i));
        }
        Export_Factory.exportARFF_ALLINONE(sps);
        Export_Factory.exportExcel(sps);
    }

    @Test
    public void ConvertExcelAndOutputARFF_Folder9(){
        String folderPath = "D:\\RIL_Excel\\9\\";
        String file = "all_file_top100.xls";

        List<SubjectProperty> sps = readExcel_Folder9(folderPath + file);


        for(int i=0; i<sps.size();i++){
            Export_Factory.exportARFF(sps.get(i));
        }
        Export_Factory.exportARFF_ALLINONE(sps);
    }
}