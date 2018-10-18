package ril.facotry;
import org.apache.poi.ss.usermodel.*;
import ril.Object;
import ril.SubjectProperty;
import ril.implementation.SubjectPropertyImplementation;

import java.io.File;
import java.util.ArrayList;

public class Import_Factory {

     public static SubjectProperty readExcel(String filePath){
         // Creating a Workbook from an Excel file (.xls or .xlsx)
         Workbook workbook;
         try {
             workbook = WorkbookFactory.create(new File(filePath));
         }catch(Exception e){
             System.out.println("Filepath error! path: " + filePath);
             return null;
         }

        /*
           ==================================================================
           Iterating over all the rows and columns in a Sheet (Multiple ways)
           ==================================================================
        */

        // Getting the Sheet at index zero
         Sheet sheet = workbook.getSheetAt(0);

         // Create a DataFormatter to format and get each cell's value as String
         DataFormatter dataFormatter = new DataFormatter();

         // Initial data for the first 2 rows.
         Row row0 = sheet.getRow(0);

         String[] subjectProperty_Labels = dataFormatter.formatCellValue(row0.getCell(0)).split("/");
         if(subjectProperty_Labels.length != 2){
             System.out.println("Error while parsing excel cell(0,0)");
             return null;
         }
         String subject_label = subjectProperty_Labels[0];
         String property_label = subjectProperty_Labels[1];

         String[] subjectProperty_IDs = dataFormatter.formatCellValue(row0.getCell(1)).split("/");
         if(subjectProperty_Labels.length != 2){
             System.out.println("Error while parsing excel cell(0,1)");
             return null;
         }
         String subject_ID = subjectProperty_IDs[0];
         String property_ID = subjectProperty_IDs[1];

         SubjectPropertyImplementation sp = new SubjectPropertyImplementation(subject_ID,property_ID,subject_label,property_label);

         ArrayList<Object> objects = new ArrayList<>();
         // Use a for-each loop to iterate over the rows and columns
         for (Row row: sheet) {
             if(row.getRowNum() <=1){
                 continue;
             }
             if(row.getRowNum() >=2) {
                 String object_label = dataFormatter.formatCellValue(row.getCell(0));
                 String object_ID = dataFormatter.formatCellValue(row.getCell(1));
                 Object obj = new Object(subject_ID,property_ID,object_ID,object_label);

                 int importance = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(2)));
                 obj.setImportance(importance);
                 int rank_importance = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(3)));
                 obj.setRank_importance(rank_importance);

                 double co_occurrence_coefficient_Bing = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(4)));
                 obj.setCo_occur_coeff(co_occurrence_coefficient_Bing, SubjectProperty.API_Occurrence.Bing);
                 int rank_co_occurrence_coefficient_Bing = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(5)));
                 obj.setRank_co_occur_coeff(rank_co_occurrence_coefficient_Bing, SubjectProperty.API_Occurrence.Bing);
                 int co_occurrence_Bing = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(6)));
                 obj.setCo_Occurrence(co_occurrence_Bing, SubjectProperty.API_Occurrence.Bing);
                 int occurrence_Bing = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(7)));
                 obj.setOccurrence(occurrence_Bing, SubjectProperty.API_Occurrence.Bing);

                 double co_occurrence_coefficient_Wikipedia = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(14)));
                 obj.setCo_occur_coeff(co_occurrence_coefficient_Wikipedia, SubjectProperty.API_Occurrence.Wikipedia);
                 int rank_co_occurrence_coefficient_Wikipedia = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(15)));
                 obj.setRank_co_occur_coeff(rank_co_occurrence_coefficient_Wikipedia, SubjectProperty.API_Occurrence.Wikipedia);
                 int co_occurrence_Wikipedia = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(16)));
                 obj.setCo_Occurrence(co_occurrence_Wikipedia, SubjectProperty.API_Occurrence.Wikipedia);
                 int occurrence_Wikipedia = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(17)));
                 obj.setOccurrence(occurrence_Wikipedia, SubjectProperty.API_Occurrence.Wikipedia);
                 int countTriple = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(23)));
                 obj.setCountTriple(countTriple);
                 int bol = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(24)));
                 if(bol == 1){
                     obj.setPositive_obj(true);
                 }else{
                     obj.setPositive_obj(false);
                 }
                 double groundTruth = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(25)));
                 obj.setGroundTruth(groundTruth);
                 objects.add(obj);
             }
         }
         sp.setObjects(objects);
         return sp;
     }

}
