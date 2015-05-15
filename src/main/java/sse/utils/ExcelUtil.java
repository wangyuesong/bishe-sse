package sse.utils;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import sse.commandmodel.MatchPair;

/**
 * @Project: sse
 * @Title: ExcelUtil.java
 * @Package sse.utils
 * @Description: TODO
 * @author YuesongWang
 * @date 2015年5月15日 下午3:03:43
 * @version V1.0
 */
public class ExcelUtil<J> {

    /** 
     * Description: TODO
     * @param wb
     * @param cols
     * @param sheetName
     * @param contents
     * void
     */
    public void wrtieWorkbook(Workbook wb, List<String> cols, String sheetName, List<J> contents)
    {
        ClassUtil<J> classUtil = new ClassUtil<J>();
        Sheet sheet = wb.createSheet(sheetName);
        short rowNum = 0;
        short colNum = 0;
        Row row;
        int count = contents.size();
        for (rowNum = 0; rowNum < count; rowNum++)
        {
            J currentRow = contents.get(rowNum);
            row = sheet.createRow(rowNum);
            for (String colName : cols)
            {
                row.createCell(colNum++).setCellValue(classUtil.callGetMethod(currentRow, colName));
            }
            colNum = 0;
        }
    }
}
