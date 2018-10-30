package ril.facotry;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import ril.Object;
import ril.SubjectProperty;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Export_Factory {

    /**
     * Output the first line, which we need to output in the excel.
     * @param sp The data
     * @return
     */
    public static List<String> first_Line_Excel (SubjectProperty sp){
        List<String> comments = new ArrayList<>();
        comments.add(sp.getSubject_label() + "/" + sp.getProperty_label());         //0
        comments.add(sp.getSubject_id() + "/" + sp.getProperty_id());               //1
        comments.add("GroundTruth Average");                                      //2
        comments.add("Is this positive?");                                        //3
        comments.add("Viewers");                                                   //4
        comments.add("Ranked by Viewers");                                        //5
        comments.add("CO_OCCURRENCE / OCCURRENCE Bing");                         //6
        comments.add("Ranked by CO_OCCURRENCE / OCCURRENCE Bing");              //7
        comments.add("CO_OCCURRENCE on Bing");                                   //8
        comments.add("OCCURRENCE on Bing");                                       //9
        comments.add("CO_OCCURRENCE / OCCURRENCE Wikipedia");                   //10
        comments.add("Ranked by CO_OCCURRENCE / OCCURRENCE Wikipedia");        //11
        comments.add("CO_OCCURRENCE on Wikipedia");                             //12
        comments.add("OCCURRENCE on Wikipedia");                                //13
        comments.add("Count the number of facts for an object");               //14
        comments.add("Ranked by countFacts");                                    //15
        comments.add("Count the number of superclass");                         //16
        comments.add("Ranked by NumSuperclass");                                 //17
        comments.add("Count the number of subclass");                           //18
        comments.add("Ranked by NumSubclass");                                   //19
        comments.add("Count the distance between subject and object");         //20
        comments.add("Ranked by distance");                                      //21
        comments.add("Count the number of Subject given Property and Object"); //22
        comments.add("Ranked by numSubject");                                    //23
        comments.add("Count the number of Subject and Property pair given Object"); //24
        comments.add("Ranked by numSubjectProperty");                               //25
        return comments;
    }

    /**
     * Out put the list of string, which we will output in the second line of excel.
     * @param sp
     * @return
     */
    public static List<String> second_Line_Excel (SubjectProperty sp){
        List<String> comments = new ArrayList<>();
        comments.add("Labels of objects");                                  //0
        comments.add("ID of objects");                                      //1
        comments.add("GroundTruth");                                        //2
        comments.add("isPositive");                                         //3
        comments.add("Importance");                                         //4
        comments.add("Ranked by Importance");                              //5
        comments.add("CO_OCCURRENCE_COEFFICIENT_Bing");                   //6
        comments.add("Ranked by CO_OCCURRENCE_COEFFICIENT_Bing");        //7
        comments.add("CO_OCCURRENCE_Bing");                                //8
        comments.add("OCCURRENCE_Bing");                                    //9
        comments.add("CO_OCCURRENCE_COEFFICIENT_Wikipedia");              //10
        comments.add("Ranked by CO_OCCURRENCE_COEFFICIENT_Wikipedia");   //11
        comments.add("CO_OCCURRENCE_Wikipedia");                          //12
        comments.add("OCCURRENCE_Wikipedia");                             //13
        comments.add("countFacts");                                        //14
        comments.add("Ranked by countFacts");                             //15
        comments.add("NumSuperclass");                                     //16
        comments.add("Ranked by NumSuperclass");                          //17
        comments.add("NumSubclass");                                        //18
        comments.add("Ranked by NumSubclass");                             //19
        comments.add("distance");                                           //20
        comments.add("Ranked by distance");                                //21
        comments.add("numSubject");                                         //22
        comments.add("Ranked by numSubject");                              //23
        comments.add("numSubjectProperty");                                 //24
        comments.add("Ranked by numSubjectProperty");                      //25
        return comments;
    }

    public static void exportExcel(List<SubjectProperty> sps){
        HSSFWorkbook excel = new HSSFWorkbook();
        for(SubjectProperty sp : sps) {
            List<Object> objects = sp.getObjects();
            HSSFSheet hssfSheet = excel.createSheet(sp.getSubject_id() + " " + sp.getProperty_id());
            HSSFRow row1 = hssfSheet.createRow(0);
            List<String> first_line_excel = first_Line_Excel(sp);
            for(int i=0; i< first_line_excel.size(); i++){
                row1.createCell(i).setCellValue(first_line_excel.get(i));
            }

            HSSFRow row2 = hssfSheet.createRow(1);
            List<String> second_line_excel = second_Line_Excel(sp);
            for(int i=0; i< second_line_excel.size(); i++){
                row2.createCell(i).setCellValue(second_line_excel.get(i));
            }

            for (int rowNum = 0; rowNum < objects.size(); rowNum++) {
                HSSFRow row = hssfSheet.createRow(rowNum + 2);
                Object obj = objects.get(rowNum);

                row.createCell(0).setCellValue(obj.getObject_label());
                row.createCell(1).setCellValue(obj.getObject_ID());
                row.createCell(2).setCellValue(obj.getGroundTruth());
                int pos = obj.isPositive_obj() ? 1 : 0;
                row.createCell(3).setCellValue(pos);
                row.createCell(4).setCellValue(obj.getImportance());
                row.createCell(5).setCellValue(obj.getRank_importance());

                SubjectProperty.API_Occurrence api = SubjectProperty.API_Occurrence.Bing;
                row.createCell(6).setCellValue(obj.getCo_occur_coeff(api));
                row.createCell(7).setCellValue(obj.getRank_co_occur_coeff(api));
                row.createCell(8).setCellValue(obj.getCo_Occurrence(api));
                row.createCell(9).setCellValue(obj.getOccurrence(api));

                api = SubjectProperty.API_Occurrence.Wikipedia;
                row.createCell(10).setCellValue(obj.getCo_occur_coeff(api));
                row.createCell(11).setCellValue(obj.getRank_co_occur_coeff(api));
                row.createCell(12).setCellValue(obj.getCo_Occurrence(api));
                row.createCell(13).setCellValue(obj.getOccurrence(api));

                row.createCell(14).setCellValue(obj.getCountFacts());
                row.createCell(15).setCellValue(obj.getRank_CountFacts());
                row.createCell(16).setCellValue(obj.getNumSuperclass());
                row.createCell(17).setCellValue(obj.getRank_NumSuperclass());
                row.createCell(18).setCellValue(obj.getNumSubclass());
                row.createCell(19).setCellValue(obj.getRank_NumSubclass());
                row.createCell(20).setCellValue(obj.getDistance());
                row.createCell(21).setCellValue(obj.getRank_Distance());
                row.createCell(22).setCellValue(obj.getNumSubject());
                row.createCell(23).setCellValue(obj.getRank_NumSubject());
                row.createCell(24).setCellValue(obj.getNumSubjectProperty());
                row.createCell(25).setCellValue(obj.getRank_NumSubjectProperty());
            }
            System.out.println(hssfSheet.getSheetName() + " created successfully.");
        }
        try {
            FileOutputStream fout = new FileOutputStream("D:\\RIL_Excel\\9\\" + "all_file_top100.xls");
            excel.write(fout);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportARFF (SubjectProperty sp){
        List<Object> objects = sp.getObjects();

        String str = "";
        str = str + "@relation " + sp.getSubject_id() + sp.getProperty_id() + "\n\n";
        str = str + "@attribute Importance numeric\n";                      //1
        str = str + "@attribute Co_Occur_Coeff_Bing numeric\n";            //2
        str = str + "@attribute Co_Occur_Coeff_Wikipedia numeric\n";       //3
        str = str + "@attribute CountFacts numeric\n";                      //4
        str = str + "@attribute NumSuperclass numeric\n";                  //5
        str = str + "@attribute NumSubclass numeric\n";                    //6
        str = str + "@attribute Distance numeric\n";                        //7
        str = str + "@attribute NumSubject numeric\n";                      //8
        str = str + "@attribute NumSubjectProperty numeric\n";              //9
        str = str + "@attribute isPositiv numeric \n";                      //10
        str = str + "@attribute GroundTruth numeric\n\n";

        str = str + "@data\n";
        List<List<Object>> lists = splitList(objects,0.5);
        List<Object> list_1 = lists.get(0);
        List<Object> list_2 = lists.get(1);
        int minSize = list_1.size() > list_2.size() ? list_2.size() : list_1.size();

        for(int i=0; i<minSize * 2;i++){
            Object obj;
            if( i % 2 == 0){
                int randomNum = ThreadLocalRandom.current().nextInt(0, list_1.size());
                obj = list_1.get(randomNum);
                list_1.remove(randomNum);
            }
            else{
                int randomNum = ThreadLocalRandom.current().nextInt(0, list_2.size());
                obj = list_2.get(randomNum);
                list_2.remove(randomNum);
            }
            str = str + obj.getImportance() + ",";                                                 //1
            str = str + obj.getCo_occur_coeff(SubjectProperty.API_Occurrence.Bing) + ",";          //2
            str = str + obj.getCo_occur_coeff(SubjectProperty.API_Occurrence.Wikipedia) + ",";     //3
            str = str + obj.getCountFacts() + ",";                                                 //4
            str = str + obj.getNumSuperclass() + ",";                                              //5
            str = str + obj.getNumSubclass() + ",";                                                //6
            str = str + obj.getDistance() + ",";                                                   //7
            str = str + obj.getNumSubject() + ",";                                                  //8
            str = str + obj.getNumSubjectProperty() + ",";                                          //9
            str = str + (obj.isPositive_obj() ? 1 : 0) + ",";                                     //10
            str = str + obj.getGroundTruth() + "\n";
        }
        String fileName = "D:\\RIL_Excel\\10\\" + sp.getSubject_id() + "_" + sp.getProperty_id() + "_2.arff";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
        }catch (Exception e){
            System.out.println("ERROR while writing ARFF file");
            e.printStackTrace();
            return;
        }
    }

    public static void exportARFF_ALLINONE (List<SubjectProperty> sps){

        String str = "";
        str = str + "@relation  SubjectProperty\n\n";
        str = str + "@attribute Importance numeric\n";                      //1
        str = str + "@attribute Co_Occur_Coeff_Bing numeric\n";            //2
        str = str + "@attribute Co_Occur_Coeff_Wikipedia numeric\n";       //3
        str = str + "@attribute CountFacts numeric\n";                      //4
        str = str + "@attribute NumSuperclass numeric\n";                  //5
        str = str + "@attribute NumSubclass numeric\n";                    //6
        str = str + "@attribute Distance numeric\n";                        //7
        str = str + "@attribute NumSubject numeric\n";                      //8
        str = str + "@attribute NumSubjectProperty numeric\n";              //9
        str = str + "@attribute isPositiv numeric \n";                      //10
        str = str + "@attribute GroundTruth numeric\n\n";

        str = str + "@data\n";

        List<Object> all_objects = new ArrayList<>();
        for(int index = 0; index < sps.size(); index ++){
            all_objects.addAll(sps.get(index).getObjects());
        }
        List<List<Object>> lists = splitList(all_objects,0.5);
        List<Object> list_1 = lists.get(0);
        List<Object> list_2 = lists.get(1);
        int minSize = list_1.size() > list_2.size() ? list_2.size() : list_1.size();

        for(int i=0; i<minSize * 2;i++){
            Object obj;
            if( i % 2 == 0){
                int randomNum = ThreadLocalRandom.current().nextInt(0, list_1.size());
                obj = list_1.get(randomNum);
                list_1.remove(randomNum);
            }
            else{
                int randomNum = ThreadLocalRandom.current().nextInt(0, list_2.size());
                obj = list_2.get(randomNum);
                list_2.remove(randomNum);
            }
            str = str + obj.getImportance() + ",";                                             //1
            str = str + obj.getCo_occur_coeff(SubjectProperty.API_Occurrence.Bing) + ",";     //2
            str = str + obj.getCo_occur_coeff(SubjectProperty.API_Occurrence.Wikipedia) + ",";//3
            str = str + obj.getCountFacts() + ",";                                            //4
            str = str + obj.getNumSuperclass() + ",";                                         //5
            str = str + obj.getNumSubclass() + ",";                                          //6
            str = str + obj.getDistance() + ",";                                                  //7
            str = str + obj.getNumSubject() + ",";                                                 //8
            str = str + obj.getNumSubjectProperty() + ",";                                         //9
            str = str + (obj.isPositive_obj() ? 1 : 0) + ",";                                   //10
            str = str + obj.getGroundTruth() + "\n";
        }
        String fileName = "D:\\RIL_Excel\\10\\SubjectProperty_ALL_2.arff";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
        } catch (Exception e) {
            System.out.println("ERROR while writing ARFF file");
            e.printStackTrace();
            return;
        }
    }

    private static List<List<Object>> splitList (List<Object> objects, double splitPoint){
        List<List<Object>> lists = new ArrayList<>();
        List<Object> list_1 = new ArrayList<>();
        List<Object> list_2 = new ArrayList<>();
        for(Object obj : objects){
            if(obj.getGroundTruth() < splitPoint){
                list_1.add(obj);
            }else{
                list_2.add(obj);
            }
        }
        lists.add(list_1);
        lists.add(list_2);
        return lists;
    }
}
