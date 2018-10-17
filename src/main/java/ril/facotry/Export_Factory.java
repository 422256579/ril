package ril.facotry;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import ril.Object;
import ril.SubjectProperty;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class Export_Factory {

    public static void exportExcel(SubjectProperty sp){
        List<Object> objects = sp.getObjects();

        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet hssfSheet = excel.createSheet(sp.getSubject_id() + " " + sp.getProperty_id());
        HSSFRow row1 = hssfSheet.createRow(0);

        //First row
        row1.createCell(0).setCellValue(sp.getSubject_label() + "/"+ sp.getProperty_label());
        row1.createCell(1).setCellValue(sp.getSubject_id() + "/" + sp.getProperty_id());
        row1.createCell(2).setCellValue("Viewers");
        row1.createCell(3).setCellValue("Ranked by Viewers");
        row1.createCell(4).setCellValue("CO_OCCURRENCE / OCCURRENCE Bing");
        row1.createCell(5).setCellValue("Ranked by CO_OCCURRENCE / OCCURRENCE Bing");
        row1.createCell(6).setCellValue("CO_OCCURRENCE on Bing");
        row1.createCell(7).setCellValue("OCCURRENCE on Bing");
        row1.createCell(8).setCellValue("Ranked by IMPORTANCE * 0.5 + Ranked by CO_OCCUR_COEFF * 0.5");
        row1.createCell(9).setCellValue("Ranked by IMPORTANCE * 1/3 + Ranked by CO_OCCUR_COEFF * 2/3");
        row1.createCell(10).setCellValue("Ranked by IMPORTANCE * 2/3 + Ranked by CO_OCCUR_COEFF * 1/3");
        row1.createCell(11).setCellValue("Ranked by IMPORTANCE * 1/4 + Ranked by CO_OCCUR_COEFF * 3/4");
        row1.createCell(12).setCellValue("Ranked by IMPORTANCE * 3/4 + Ranked by CO_OCCUR_COEFF * 1/4");

        row1.createCell(13).setCellValue("");
        row1.createCell(14).setCellValue("CO_OCCURRENCE / OCCURRENCE Wikipedia");
        row1.createCell(15).setCellValue("Ranked by CO_OCCURRENCE / OCCURRENCE Wikipedia");
        row1.createCell(16).setCellValue("CO_OCCURRENCE on Wikipedia");
        row1.createCell(17).setCellValue("OCCURRENCE on Wikipedia");
        row1.createCell(18).setCellValue("Ranked by IMPORTANCE * 0.5 + Ranked by CO_OCCUR_COEFF * 0.5");
        row1.createCell(19).setCellValue("Ranked by IMPORTANCE * 1/3 + Ranked by CO_OCCUR_COEFF * 2/3");
        row1.createCell(20).setCellValue("Ranked by IMPORTANCE * 2/3 + Ranked by CO_OCCUR_COEFF * 1/3");
        row1.createCell(21).setCellValue("Ranked by IMPORTANCE * 1/4 + Ranked by CO_OCCUR_COEFF * 3/4");
        row1.createCell(22).setCellValue("Ranked by IMPORTANCE * 3/4 + Ranked by CO_OCCUR_COEFF * 1/4");
        row1.createCell(23).setCellValue("Count the number of facts for an object");
        row1.createCell(24).setCellValue("Is this a positive fact?");

        HSSFRow row2 = hssfSheet.createRow(1);

            //Second row
        row2.createCell(0).setCellValue("Labels of objects");
        row2.createCell(1).setCellValue("");
        row2.createCell(2).setCellValue("IMPORTANCE");
        row2.createCell(3).setCellValue("Ranked by IMPORTANCE");
        row2.createCell(4).setCellValue("CO_OCCUR_COEFF_Bing");
        row2.createCell(5).setCellValue("Ranked by CO_OCCUR_COEFF_Bing");
        row2.createCell(6).setCellValue("CO_OCCURRENCE_Bing");
        row2.createCell(7).setCellValue("OCCURRENCE_Bing");

        row2.createCell(8).setCellValue("1:1 Weighted_Bing");
        row2.createCell(9).setCellValue("1:2 Weighted_Bing");
        row2.createCell(10).setCellValue("2:1 Weighted_Bing");
        row2.createCell(11).setCellValue("1:3 Weighted_Bing");
        row2.createCell(12).setCellValue("3:1 Weighted_Bing");

        row2.createCell(13).setCellValue("");
        row2.createCell(14).setCellValue("CO_OCCUR_COEFF_Wikipedia");
        row2.createCell(15).setCellValue("Ranked by CO_OCCUR_COEFF_Wikipedia");
        row2.createCell(16).setCellValue("CO_OCCURRENCE_Wikipedia");
        row2.createCell(17).setCellValue("OCCURRENCE_Wikipedia");
        row2.createCell(18).setCellValue("1:1 Weighted_Wikipedia");
        row2.createCell(19).setCellValue("1:2 Weighted_Wikipedia");
        row2.createCell(20).setCellValue("2:1 Weighted_Wikipedia");
        row2.createCell(21).setCellValue("1:3 Weighted_Wikipedia");
        row2.createCell(22).setCellValue("3:1 Weighted_Wikipedia");
        row2.createCell(23).setCellValue("Additional Feature");
        row2.createCell(24).setCellValue("isPositive");

        for(int rowNum=0; rowNum<objects.size(); rowNum++){
            HSSFRow row = hssfSheet.createRow(rowNum+2);
            Object obj = objects.get(rowNum);

            row.createCell(0).setCellValue(obj.getObject_label());
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue(obj.getImportance());
            row.createCell(3).setCellValue(obj.getRank_importance());
            SubjectProperty.API_Occurrence api = SubjectProperty.API_Occurrence.Bing;
            row.createCell(4).setCellValue(obj.getCo_occur_coeff(api));
            row.createCell(5).setCellValue(obj.getRank_co_occur_coeff(api));
            row.createCell(6).setCellValue(obj.getCo_Occurrence(api));
            row.createCell(7).setCellValue(obj.getOccurrence(api));

            row.createCell(8).setCellValue(cal_weighted(obj.getRank_importance(),obj.getRank_co_occur_coeff(api),0.5));
            row.createCell(9).setCellValue(cal_weighted(obj.getRank_importance(),obj.getRank_co_occur_coeff(api),1.0/3));
            row.createCell(10).setCellValue(cal_weighted(obj.getRank_importance(),obj.getRank_co_occur_coeff(api),2.0/3));
            row.createCell(11).setCellValue(cal_weighted(obj.getRank_importance(),obj.getRank_co_occur_coeff(api),0.25));
            row.createCell(12).setCellValue(cal_weighted(obj.getRank_importance(),obj.getRank_co_occur_coeff(api),0.75));

            api = SubjectProperty.API_Occurrence.Wikipedia;
            row.createCell(13).setCellValue("");
            row.createCell(14).setCellValue(obj.getCo_occur_coeff(api));
            row.createCell(15).setCellValue(obj.getRank_co_occur_coeff(api));
            row.createCell(16).setCellValue(obj.getCo_Occurrence(api));
            row.createCell(17).setCellValue(obj.getOccurrence(api));
            row.createCell(18).setCellValue(cal_weighted(obj.getRank_importance(),obj.getRank_co_occur_coeff(api),0.5));
            row.createCell(19).setCellValue(cal_weighted(obj.getRank_importance(),obj.getRank_co_occur_coeff(api),1.0/3));
            row.createCell(20).setCellValue(cal_weighted(obj.getRank_importance(),obj.getRank_co_occur_coeff(api),2.0/3));
            row.createCell(21).setCellValue(cal_weighted(obj.getRank_importance(),obj.getRank_co_occur_coeff(api),0.25));
            row.createCell(22).setCellValue(cal_weighted(obj.getRank_importance(),obj.getRank_co_occur_coeff(api),0.75));
            row.createCell(23).setCellValue(obj.getCount_triple());
            int pos = obj.isPositive_obj() ? 1:0;
            row.createCell(24).setCellValue(pos);
        }
        try {
            FileOutputStream fout = new FileOutputStream("D:\\RIL_Excel\\3\\" + sp.getSubject_id() + "_" + sp.getProperty_id() + "_top100.xls" );
            excel.write(fout);
            fout.close();
            System.out.println(hssfSheet.getSheetName() + " created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String cal_weighted(int rank_1, int rank_2, double weighted){
        DecimalFormat df = new DecimalFormat("#.##");
        String x = df.format(rank_1 * weighted + rank_2 * (1-weighted));
        return x;
    }


}
