package com.kylin.core.common;

import com.kylin.core.util.ComUtil;
import com.kylin.core.util.FileUtil;
import com.sun.media.sound.InvalidFormatException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author liugh
 * @since on 2018/5/8.
 */

public class ExcelHelper {
    /**
     * 获得excel文件对象
     *
     * @param excelFile
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static Workbook getExcelWorkBook(File excelFile) throws IOException, InvalidFormatException {
        InputStream excelInput = new FileInputStream(excelFile);
        /**
         * HSSF － 提供读写Microsoft Excel格式档案的功能。 XSSF － 提供读写Microsoft Excel
         * OOXML格式档案的功能 统一解决方式用WorkbookFactory创建
         */
//		return WorkbookFactory.create(excelInput);TODO
        return null;
    }

    /**
     * 获得sheet页数量
     *
     * @param workbook
     * @return
     */
    public static int getExcelSheets(Workbook workbook) {
        return workbook.getNumberOfSheets();
    }

    /**
     * 获得sheet也可读取行数
     *
     * @param sheet
     * @return
     */
    public static int getSheetRows(Sheet sheet) {
        return sheet.getLastRowNum() + 1;
    }

    /**
     * 获得可读取行数的可读取列数
     *
     * @param row
     * @return
     */
    public static int getRowColumns(Row row) {
        return row.getLastCellNum();
    }

    /**
     * TODO.获取workbook中的所有sheet
     *
     * @param workbook
     * @return
     */
    public static List<Sheet> getSheetsFromWorkbook(Workbook workbook) {
        List<Sheet> result = new ArrayList<Sheet>();
        int sheetCounts = workbook.getNumberOfSheets();
        Sheet currentSheet = null;
        for (int i = 0; i < sheetCounts; i++) {
            currentSheet = workbook.getSheetAt(i);
            if(currentSheet == null) {
                continue;
            }
            result.add(currentSheet);
        }
        return result;
    }

    public static List<Row> getRowsFromSheet(Sheet sheet) {
        List<Row> rows = new ArrayList<Row>();
        int rowCounts = sheet.getLastRowNum() + 1;
        for (int i = 0; i < rowCounts; i++) {
            rows.add(sheet.getRow(i));
        }
        return rows;
    }

    public static List<Cell> getCellsFromRow(Row row) {
        List<Cell> cells = new ArrayList<Cell>();
        short minColIx = row.getFirstCellNum();
        short maxColIx = row.getLastCellNum();
        for (short colIx = minColIx; colIx < maxColIx; colIx++) {
            cells.add(row.getCell(colIx));
        }
        return cells;
    }

    public static List<List<Cell>> getRowCellsFromSheet(Sheet sheet){
        List<Row> rows = ExcelHelper.getRowsFromSheet(sheet);
        List<List<Cell>> result = new ArrayList<List<Cell>>();
        for(Row row : rows){
            result.add(getCellsFromRow(row));
        }
        return result;
    }

    public static Cell[][] getRowCellsArrayFromSheet(Sheet sheet){
        Row[] rows = ExcelHelper.getRowsFromSheet(sheet).toArray(new Row[]{});
        List<Cell[]> result = new ArrayList<>();
        for(Row row : rows){
            result.add(getCellsFromRow(row).toArray(new Cell[]{}));
        }
        return result.toArray(new Cell[][]{});
    }

    public static List<List<Cell>> getColumnsFromSheet(Sheet sheet){
        List<Row> rows = ExcelHelper.getRowsFromSheet(sheet);
        List<List<Cell>> result = new ArrayList<List<Cell>>();
        List<Cell> currentColumn = null;
        Cell currentCell = null;
        int columnCount = determineColumnCount(sheet);
        for(int i = 0; i < columnCount; i++){
            currentColumn = new ArrayList<Cell>();
            for(Row row : rows){
                currentCell = row.getCell(i);
                if(!ComUtil.isEmpty(currentCell)
                        && !ComUtil.isEmpty(ExcelHelper.getValue(currentCell))){
                    currentColumn.add(currentCell);
                }
            }
            result.add(currentColumn);
        }
        return result;
    }

    public static int determineColumnCount(Sheet sheet){
        List<Row> rows = ExcelHelper.getRowsFromSheet(sheet);
        if(!ComUtil.isEmpty(rows)){
            Row firstRow = rows.get(0);
            for(int i = firstRow.getLastCellNum();i > 0; i--){
                if(ComUtil.isEmpty(firstRow.getCell(i)) || ComUtil.isEmpty(ExcelHelper.getValue(firstRow.getCell(i)))) {
                    continue;
                }
                return i + 1;
            }
        }
        return 0;
    }

    /**
     * TODO.加载excel的数据结构到内存中
     *
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static List<List<Row>> loadExcel2Memory(File excelFile) throws InvalidFormatException, IOException {
        List<List<Row>> excel = new ArrayList<List<Row>>();
        List<Sheet> sheets = getSheetsFromWorkbook(getExcelWorkBook(excelFile));
        for (Sheet sheet : sheets) {
            excel.add(getRowsFromSheet(sheet));
        }
        return excel;
    }
    /**
     * TODO.获取工作簿中的所有图片
     * @param workbook
     * @return
     */
    public static List<? extends PictureData> getAllPicsFromWorkbook(Workbook workbook){
        List<? extends PictureData> result = workbook.getAllPictures();
        return result;
    }

    public static String getValue(Cell cell)
    {
        String value = "";
        switch (cell.getCellType())
        {
            // 数值型
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell))
                {
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    value = format.format(date);
                }
                else
                {
                    value = String.valueOf(cell.getNumericCellValue());
                }
                break;
            // 此行表示单元格的内容为string类型
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            // 公式型
            case Cell.CELL_TYPE_FORMULA:
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN"))
                {
                    value = cell.getStringCellValue().toString();
                }
                cell.getCellFormula();
                break;
            // 布尔
            case Cell.CELL_TYPE_BOOLEAN:
                value = "" + cell.getBooleanCellValue();
                break;
            // 此行表示该单元格值为空
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            // 故障
            case Cell.CELL_TYPE_ERROR:
                value = "";
                break;
            default:
                value = cell.getStringCellValue().toString();
        }
        return value;
    }


    public static String exportExcel(Collection<Map<String,Object>> collection, Map<String,String> supplierKeys, String fileName, ResultProcessor processor) throws Exception {
        String outFileName = fileName + ".xls";
        String pathName = System.getProperty("java.io.tmpdir") + outFileName;

        OutputStream out = new FileOutputStream(pathName);
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(20);

        try {
            Row row = sheet.createRow(0);
            int i = 0;
            for (String detail1 : supplierKeys.keySet()) {
                row.createCell(i++).setCellValue(supplierKeys.get(detail1));
            }
            int n = 1;
            Iterator<Map<String,Object>> iter = collection.iterator();
            while(iter.hasNext()){
                Map<String, Object> detail = iter.next();
                Row rowAcross = sheet.createRow(n++);
                int j = 0;
                for (String details : supplierKeys.keySet()) {
                    Object value = detail.get(details);
                    rowAcross.createCell(j++)
                            .setCellValue(ComUtil.isEmpty(value) ? "" : value.toString());
                }
            }

            workbook.write(out);
            File demandFile = new File(pathName);
            FileInputStream fileInputStream = new FileInputStream(demandFile);
            //处理导出结果
            processor.call(fileInputStream);

            fileInputStream.close();
            demandFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
            FileUtil.deleteFile(pathName);
        }

        return outFileName;

    }

    public interface ResultProcessor {
        void call(FileInputStream fileInputStream) throws Exception;
    }


}
