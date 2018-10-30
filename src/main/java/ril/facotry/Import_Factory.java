package ril.facotry;

import org.apache.poi.ss.usermodel.*;
import ril.Object;
import ril.SubjectProperty;
import ril.implementation.SubjectPropertyImplementation;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Import_Factory {

     public static List<SubjectProperty> readExcel_Folder6(String filePath){
         // Creating a Workbook from an Excel file (.xls or .xlsx)
         Workbook workbook;
         try {
             workbook = WorkbookFactory.create(new File(filePath));
         }catch(Exception e){
             System.out.println("Filepath error! path: " + filePath);
             return null;
         }

         List<SubjectProperty> sps = new ArrayList<>();

        for(int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            // Getting the Sheet at index zero
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();

            // Initial data for the first 2 rows.
            Row row0 = sheet.getRow(0);

            String[] subjectProperty_Labels = dataFormatter.formatCellValue(row0.getCell(0)).split("/");
            if (subjectProperty_Labels.length != 2) {
                System.out.println("Error while parsing excel cell(0,0)");
                return null;
            }
            String subject_label = subjectProperty_Labels[0];
            String property_label = subjectProperty_Labels[1];

            String[] subjectProperty_IDs = dataFormatter.formatCellValue(row0.getCell(1)).split("/");
            if (subjectProperty_Labels.length != 2) {
                System.out.println("Error while parsing excel cell(0,1)");
                return null;
            }
            String subject_ID = subjectProperty_IDs[0];
            String property_ID = subjectProperty_IDs[1];

            SubjectPropertyImplementation sp = new SubjectPropertyImplementation(subject_ID, property_ID, subject_label, property_label);

            ArrayList<Object> objects = new ArrayList<>();
            // Use a for-each loop to iterate over the rows and columns
            for (Row row : sheet) {
                if (row.getRowNum() <= 1) {
                    continue;
                }
                if (row.getRowNum() >= 2) {
                    String object_label = dataFormatter.formatCellValue(row.getCell(0));
                    if (object_label == "") {
                        break;
                    }
                    String object_ID = dataFormatter.formatCellValue(row.getCell(1));
                    Object obj = new Object(subject_ID, property_ID, object_ID, object_label);

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
                    obj.setCountFacts(countTriple);
                    int bol = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(24)));
                    if (bol == 1) {
                        obj.setPositive_obj(true);
                    } else {
                        obj.setPositive_obj(false);
                    }
                    // Set groundTruth average.
                    double groundTruth = (Double.parseDouble(dataFormatter.formatCellValue(row.getCell(25))) + Double.parseDouble(dataFormatter.formatCellValue(row.getCell(26))) + Double.parseDouble(dataFormatter.formatCellValue(row.getCell(27)))) / 3;
                    obj.setGroundTruth(groundTruth);
                    objects.add(obj);
                }
            }
            sp.setObjects(objects);
            sp.sortObject(Sort_Factory.Parameter.COUNTFACTS);
            sps.add(sp);
        }
        return sps;
     }

    public static List<SubjectProperty> readExcel_Folder9(String filePath){
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(new File(filePath));
        }catch(Exception e){
            System.out.println("Filepath error! path: " + filePath);
            return null;
        }

        List<SubjectProperty> sps = new ArrayList<>();

        for(int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            DataFormatter dataFormatter = new DataFormatter();
            Row row0 = sheet.getRow(0);

            String[] subjectProperty_Labels = dataFormatter.formatCellValue(row0.getCell(0)).split("/");
            if (subjectProperty_Labels.length != 2) {
                System.out.println("Error while parsing excel cell(0,0)");
                return null;
            }
            String subject_label = subjectProperty_Labels[0];
            String property_label = subjectProperty_Labels[1];

            String[] subjectProperty_IDs = dataFormatter.formatCellValue(row0.getCell(1)).split("/");
            if (subjectProperty_Labels.length != 2) {
                System.out.println("Error while parsing excel cell(0,1)");
                return null;
            }
            String subject_ID = subjectProperty_IDs[0];
            String property_ID = subjectProperty_IDs[1];

            SubjectPropertyImplementation sp = new SubjectPropertyImplementation(subject_ID, property_ID, subject_label, property_label);

            ArrayList<Object> objects = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() <= 1) {
                    continue;
                }
                if (row.getRowNum() >= 2) {
                    String object_label = dataFormatter.formatCellValue(row.getCell(0));
                    if (object_label == "") {
                        break;
                    }
                    String object_ID = dataFormatter.formatCellValue(row.getCell(1));
                    Object obj = new Object(subject_ID, property_ID, object_ID, object_label);

                    // Set groundTruth
                    double groundTruth = (Double.parseDouble(dataFormatter.formatCellValue(row.getCell(2))));
                    obj.setGroundTruth(groundTruth);

                    // is positive
                    int bol = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(3)));
                    if (bol == 1) {
                        obj.setPositive_obj(true);
                    } else {
                        obj.setPositive_obj(false);
                    }

                    // importance
                    int importance = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(4)));
                    obj.setImportance(importance);
                    int rank_importance = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(5)));
                    obj.setRank_importance(rank_importance);

                    // co-occurrence coefficient Bing
                    double co_occurrence_coefficient_Bing = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(6)));
                    obj.setCo_occur_coeff(co_occurrence_coefficient_Bing, SubjectProperty.API_Occurrence.Bing);
                    int rank_co_occurrence_coefficient_Bing = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(7)));
                    obj.setRank_co_occur_coeff(rank_co_occurrence_coefficient_Bing, SubjectProperty.API_Occurrence.Bing);
                    int co_occurrence_Bing = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(8)));
                    obj.setCo_Occurrence(co_occurrence_Bing, SubjectProperty.API_Occurrence.Bing);
                    int occurrence_Bing = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(9)));
                    obj.setOccurrence(occurrence_Bing, SubjectProperty.API_Occurrence.Bing);

                    // co-occurrence coefficient wikipedia
                    double co_occurrence_coefficient_Wikipedia = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(10)));
                    obj.setCo_occur_coeff(co_occurrence_coefficient_Wikipedia, SubjectProperty.API_Occurrence.Wikipedia);
                    int rank_co_occurrence_coefficient_Wikipedia = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(11)));
                    obj.setRank_co_occur_coeff(rank_co_occurrence_coefficient_Wikipedia, SubjectProperty.API_Occurrence.Wikipedia);
                    int co_occurrence_Wikipedia = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(12)));
                    obj.setCo_Occurrence(co_occurrence_Wikipedia, SubjectProperty.API_Occurrence.Wikipedia);
                    int occurrence_Wikipedia = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(13)));
                    obj.setOccurrence(occurrence_Wikipedia, SubjectProperty.API_Occurrence.Wikipedia);

                    // countFacts
                    int countFacts = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(14)));
                    obj.setCountFacts(countFacts);
                    int rank_countFacts = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(15)));
                    obj.setRank_CountFacts(rank_countFacts);

                    // numSuperclass
                    int numSuperclass = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(16)));
                    obj.setNumSuperclass(numSuperclass);
                    int rank_numSuperclass = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(17)));
                    obj.setRank_NumSuperclass(rank_numSuperclass);

                    // numSubclass
                    int numSubclass = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(18)));
                    obj.setNumSubclass(numSubclass);
                    int rank_numSubclass = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(19)));
                    obj.setRank_NumSubclass(rank_numSubclass);

                    // distance
                    int distance = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(20)));
                    obj.setDistance(distance);
                    int rank_distance = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(21)));
                    obj.setRank_Distance(rank_distance);

                    // numSubject
                    int numSubject = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(22)));
                    obj.setNumSubject(numSubject);
                    int rank_numSubject = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(23)));
                    obj.setRank_NumSubject(rank_numSubject);

                    // numSubjectProperty
                    int numSubjectProperty = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(24)));
                    obj.setNumSubjectProperty(numSubjectProperty);
                    int rank_numSubjectProperty = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(25)));
                    obj.setRank_NumSubjectProperty(rank_numSubjectProperty);

                    objects.add(obj);
                }
            }
            sp.setObjects(objects);
            sps.add(sp);
        }
        return sps;
    }

     public static void readARFF_weka (String filepath){
         BufferedReader breader = null;
         try {
             breader = new BufferedReader(new FileReader(filepath));
         }catch(Exception e){
             System.out.println("ERROR while reading file: " + filepath);
             return;
         }

         Instances train = null;
         try{
             train = new Instances(breader);
             train.setClassIndex(train.numAttributes()-1);
         }catch (Exception e){
             System.out.println("Error while weka.Instances.");
             e.printStackTrace();
             return;
         }
         try {
             breader.close();
         }catch(Exception e){
             System.out.println("Error while closing BufferedReader");
             return;
         }

       //  NaiveBayes nB = new NaiveBayes();
         LinearRegression LR = new LinearRegression();
         try {
             LR.buildClassifier(train);
         }catch(Exception e){
             System.out.println("Error while building classifier for NaiveBayes.");
             e.printStackTrace();
             return;
         }
         Evaluation eval = null;
         try {
             eval = new Evaluation(train);
             eval.crossValidateModel(LR, train, 10, new Random(1));
         }catch(Exception e){
             System.out.println("ERROR while building Evalution.");
             e.printStackTrace();
             return;
         }
         System.out.println(eval.toSummaryString("\nResults"+ filepath +  "\n-----\n",true));


     }

     public static double readExcel_Fleiss_Kappa (String filePath){
         final int rater = 3;

         // Creating a Workbook from an Excel file (.xls or .xlsx)
         Workbook workbook;
         try {
             workbook = WorkbookFactory.create(new File(filePath));
         }catch(Exception e){
             System.out.println("Filepath error! path: " + filePath);
             return -1.0;
         }
         Sheet sheet = workbook.getSheetAt(0);
         DataFormatter dataFormatter = new DataFormatter();

         int countRow = 0;
         for(Row row : sheet){
             if(dataFormatter.formatCellValue(row.getCell(0)) == ""){
                 break;
             }else{
                 countRow++;
             }
         }

         int[][] data = new int[countRow-2][3];
         for(int i=0; i<data.length;i++){
             for(int j=0; j<data[0].length;j++){
                 data[i][j] = 0;
             }
         }

         for(int rowIndex = 2; rowIndex < countRow; rowIndex++){
             Row row = sheet.getRow(rowIndex);
             // If 0.0, then vote 1 to column 0. If 0.5, then vote 1 to column 0.5*2 = 1. If 1 then vote 1 to column 1.0*2=2.
             double Mang = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(25)));
             data[rowIndex-2][(int) (Mang * 2)] ++;
             double Simon = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(26)));
             data[rowIndex-2][(int) (Simon * 2)] ++;
             double Hiba = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(27)));
             data[rowIndex-2][(int) (Hiba * 2)] ++;
         }

         double[] P = new double[data.length];
         for(int i=0; i<P.length;i++){
             P[i] = 0.0;
         }
         double[] p = new double[data[0].length];
         for(int j=0; j<p.length; j++){
             p[j] = 0.0;
         }
         // compute P
         for(int i=0; i<data.length; i++){
              for(int j=0; j<data[0].length;j++){
                P[i] = P[i] + data[i][j] * (data[i][j] - 1);
             }
             P[i] = P[i] / (rater * (rater - 1)) ;
         }
        //compute p
         for(int j=0; j<data[0].length;j++){
             for(int i=0; i<data.length; i++){
                 p[j] = p[j] + data[i][j];
             }
             p[j] = p[j] / ((countRow - 2) * rater);
         }

         double sum_P = 0.0;
         for(int i=0; i<P.length; i++){
             sum_P = sum_P + P[i];
         }
         double P_line = sum_P / (countRow - 2);

         double P_line_e = 0.0;
         for(int j=0; j<p.length; j++){
             P_line_e = P_line_e + p[j] * p[j];
         }

         double k = (P_line - P_line_e)/(1 - P_line_e);

         return k;
     }

     public static int[] readExcel_countAgreement(String filePath){
         Workbook workbook;
         try {
             workbook = WorkbookFactory.create(new File(filePath));
         }catch(Exception e){
             System.out.println("Filepath error! path: " + filePath);
             return null;
         }
         Sheet sheet = workbook.getSheetAt(0);
         DataFormatter dataFormatter = new DataFormatter();

         int countRow = 0;
         for(Row row : sheet){
             if(dataFormatter.formatCellValue(row.getCell(0)) == ""){
                 break;
             }else{
                 countRow++;
             }
         }

         // countAgreement[0] for disagreement, [1] for middle agreement, [2] for agreement
         int[] countAgreement = new int[3];
         countAgreement[0] = 0; countAgreement[1] = 0; countAgreement[2] = 0;

         for(int rowIndex = 2; rowIndex < countRow; rowIndex++){
             Row row = sheet.getRow(rowIndex);
             // If 0.0, then vote 1 to column 0. If 0.5, then vote 1 to column 0.5*2 = 1. If 1 then vote 1 to column 1.0*2=2.
             double Mang = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(25)));
             double Simon = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(26)));
             double Hiba = Double.parseDouble(dataFormatter.formatCellValue(row.getCell(27)));
             if( Mang == Simon && Simon == Hiba){
                countAgreement[2]++;
             }else if(Mang == Simon || Simon == Hiba || Hiba == Mang){
                 countAgreement[1]++;
             }else{
                 countAgreement[0]++;
             }
         }
         return countAgreement;
     }
}
