package com.yj.util;

import com.yj.bases.Case;
import com.yj.bases.WriteBackData;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static String caseFilePath = "src/main/resources/cases_v4.xlsx";

    public static Map<String, Integer> caseIdRowNumMapping = new HashMap<String, Integer>();
    public static Map<String, Integer> cellNameCellNumMapping = new HashMap<String, Integer>();

    public static List<WriteBackData> writeBackDataList = new ArrayList<WriteBackData>();

    static {
        loadRowNumCellNumMapping(caseFilePath, "用例");
    }

    private static void loadRowNumCellNumMapping(String filePath, String sheetName) {
        InputStream inputStream;
        try {
            //准备输入流对象
            inputStream = new FileInputStream(new File(filePath));
            //获取workbook
            Workbook workbook = WorkbookFactory.create(inputStream);
            //获取表单对象
            Sheet sheet = workbook.getSheet(sheetName);
            //首先渠道列名与列号的映射
            //取出标题行
            Row titleRow = sheet.getRow(0);
            int lastCellNum = titleRow.getLastCellNum();
            for (int i = 0; i < lastCellNum; i++) {
                Cell cell = titleRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                String title = cell.getStringCellValue();
                title = title.substring(0, title.indexOf("("));
                int cellNum = cell.getAddress().getColumn();
                cellNameCellNumMapping.put(title, cellNum);
            }
            int lastRowNum = sheet.getLastRowNum();
            //caseId与对应行号的映射
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                //获取caseId 列 每一行行号
                Cell cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                String caseId = cell.getStringCellValue();
                int rowNum = cell.getAddress().getRow();
                caseIdRowNumMapping.put(caseId, rowNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用泛型动态加载接口信息与用例信息
     * @param filePath
     * @param sheetName
     * @param clazz
     */
    /*
    public static void loadDatas(String filePath, String sheetName, Class clazz) {
        InputStream inputStream = null;
        try {
            //准备输入流对象
            inputStream = new FileInputStream(new File(filePath));
            //获取workbook
            Workbook workbook = WorkbookFactory.create(inputStream);
            //获取表单对象
            Sheet sheet = workbook.getSheet(sheetName);
            //解析所有的数据行
            int lastRowNum = sheet.getLastRowNum();

            //拿到标题行的数据
            Row titleRow = sheet.getRow(0);
            int lastCellNum = titleRow.getLastCellNum();

            //标题列表
            String[] titles = new String[ lastCellNum ];
            for (int i = 0; i < lastCellNum; i++) {
                Cell cell = titleRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                String title = cell.getStringCellValue();
                titles[ i ] = title;
            }
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                Object object = clazz.newInstance();

//               int lastCellNum=row.getLastCellNum();
                for (int j = 0; j < lastCellNum; j++) {
                    //取出每一列
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    //设置列的数据类型
                    cell.setCellType(CellType.STRING);
                    String value = cell.getStringCellValue();

                    //需要赋值了
                    //取出此列对应的标题
                    String title = titles[ j ];
//                    if(title.equals("ApiId(接口编号)")){ 这种写法耦合性太强
//                        interfaceInfo.setInterfaceNo(value);
//                    }
                    String methodName = "set" + title.substring(0, title.indexOf("("));
                    //通过方法名拿到方法对象
                    Method method = clazz.getMethod(methodName, String.class);
                    method.invoke(object, value);
                }
                //添加到集合
                //判断对象类型，通过instanceof 关键字来实现
                if (object instanceof Case) {
                    Case cs = (Case) object;
                    CaseUtil.cases.add(cs);
                } else if (object instanceof InterfaceInfo) {
                    InterfaceInfo info = (InterfaceInfo) object;
                    HttpClientUtil.rests.add(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/
    /**
     * 用于dataprovider
     *
     * @param filePath
     * @param sheetName
     * @param cellNames
     * @return
     */
    public static Object[][] readDataByCellNames(String filePath, String sheetName, String[] cellNames) {
        InputStream inputStream = null;
        ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();
        try {
            //准备输入流对象
            inputStream = new FileInputStream(new File(filePath));
            //获取workbook
            Workbook workbook = WorkbookFactory.create(inputStream);
            //获取表单对象
            Sheet sheet = workbook.getSheet(sheetName);
            //准备map 保存标题与他所在的列索引之间的映射
            Map<String, Integer> cellNamesAndCellNumMap = new HashMap<String, Integer>();
            //取出标题行，获取所有的标题数据，以及每一个标题所在的列索引
            Row titleRow = sheet.getRow(0);
            //取出表单中列的个数
            int lastCellNum = titleRow.getLastCellNum();
            //循环取出标题行的每一列，即每一个标题
            for (int i = 0; i < lastCellNum; i++) {
                Cell cell = titleRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                //标题
                String title = cell.getStringCellValue();
                //取出当前标题列所在的列位置（列索引）
                int cellNum = cell.getAddress().getColumn();
                cellNamesAndCellNumMap.put(title, cellNum);
            }
            //获取最后一行的索引
            int lastRowNum = sheet.getLastRowNum();
            //保存每一组数据（一行相当于一组）
            groups = new ArrayList<ArrayList<String>>();
            //从第二行开始解析，读取表单中所有的数据行
            for (int i = 1; i < lastRowNum; i++) {
                //获取一行
                Row row = sheet.getRow(i);
                //如果是空行
//                if(isEmpty(row)){
//                    //调过，不处理
//                    continue;
//                }
                //处理每一行之前，先准备一个List集合，将后面读取到的列数据保存在这个list中
                ArrayList<String> cellValuesPerRow = new ArrayList<String>();
                for (int j = 0; j < cellNames.length; j++) {
                    String cellName = cellNames[ j ];
                    //根据列名，从map中获取列索引
                    int cellNum = cellNamesAndCellNumMap.get(cellName);
                    //根据列索引，取出对象
                    Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    //取出列的值
                    String value = cell.getStringCellValue();
                    //将值保存在list集合中
                    cellValuesPerRow.add(value);
                }
                //处理完一行，将此行数据添加到组中
                groups.add(cellValuesPerRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //将list集合的数据保存到二维数组中返回
        return listToArray(groups);

    }

    private static Object[][] listToArray(ArrayList<ArrayList<String>> groups) {
        //获取总共的组数
        int size1 = groups.size();
        int size2 = groups.get(0).size();
        //声明二维数组
        Object[][] datasObject = new Object[ size1 ][ size2 ];
        for (int i = 0; i < size1; i++) {
            //取出每一组
            ArrayList<String> group = groups.get(i);
            for (int j = 0; j < size2; j++) {
                //取出每一组数据
                String value = group.get(j);
                datasObject[ i ][ j ] = value;
            }
        }
        return datasObject;

    }


    /**
     * 读取指定列的数据
     *
     * @param filePath
     * @param sheetName
     * @param cellNames 列名 可选择读取部分列数据
     * @return
     */
    public static Object[][] readXlsByCellName(String filePath, String sheetName, String[] cellNames) {
        Object[][] datas = null;
        Map<String, Integer> cellTitleNameAndCellIndexMap = new HashMap<String, Integer>();
        Workbook workbook = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(filePath));
            //首先获取workbook对象
            workbook = WorkbookFactory.create(input);
            //拿到Sheet对象
            Sheet sheet = workbook.getSheet(sheetName);
            //获取所有的标题数据，，以及每个标题所在的列索引
            Row titleRow = sheet.getRow(0);
            int lastCellNum = titleRow.getLastCellNum();//获取最后一列
            System.out.println(lastCellNum);
            for (int i = 0; i < lastCellNum; i++) {
                Cell cell = titleRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                String title = cell.getStringCellValue();
                //取出当前标题列所在的位置（列索引）
                int cellNum = cell.getAddress().getColumn();
                cellTitleNameAndCellIndexMap.put(title, cellNum);
            }
            //取出所有行（标题除外）

            //获取最后一行的索引
            int lastRowNum = sheet.getLastRowNum();
            datas = new Object[ lastRowNum ][ lastCellNum ];
            ArrayList<ArrayList<String>> groups = new ArrayList<>();
            System.out.println(lastRowNum);
            for (int i = 1; i < lastRowNum; i++) {
                Row row = sheet.getRow(i);
                //取出每行对应的列数据
                for (int j = 0; j < cellNames.length; j++) {
                    String cellName = cellNames[ j ];
                    Cell cell = row.getCell(cellTitleNameAndCellIndexMap.get(cellName), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    String value = cell.getStringCellValue();
                    datas[ i - 1 ][ j ] = value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }

//    /**
//     * 读取数据
//     *
//     * @param filePath
//     * @param sheetName
//     * @return
//     */
//    public static Object[][] readXlsTest(String filePath, String sheetName) {
//        Object[][] datas=new Object[][]{};
//        try {
//            //首先获取workbook对象
//            Workbook workbook = WorkbookFactory.create(new FileInputStream(new File(filePath)));
//            //拿到Sheet对象
//            Sheet sheet = workbook.getSheet(sheetName);
//            //拿到要操作的行Row
//            int startRowNum=2;
//            int endRowNum=7;
//            int startCellNum=1;
//            int endCellNum=4;
//            for (int i=startRowNum;i<=7;i++){
//              Row row=  sheet.getRow(i-1);
//              for (int j=startCellNum;j<=endCellNum;j++){
//                 Cell cell= row.getCell(j-1,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);//为空时设置为Null
//                 cell.setCellType(CellType.STRING);//设置为字符创类型
//                  String value=cell.getStringCellValue();
//                  datas[i-startRowNum][j-startCellNum]=value;
//              }
//            }
//            //拿到要操作的列Cell
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return datas;
//    }

//    public static  boolean isEmpty(Row row){
//        int lastCellNum=row.getLastCellNum();
//        for (int i=0;i<=lastCellNum;i++){
//            Cell cell=row.getCell(i,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//            cell.setCellType(CellType.STRING);
//            String value=cell.getStringCellValue();
//            if(StringUtils.isEmpty(value)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public static void writeData(String filePath, String sheetName, String caseId, String cellName, String result) {

        System.out.println("读取数据");
        //根据caseiD获取行号
        int rowNum = caseIdRowNumMapping.get(caseId);
        //根据cellName获取列号
        int cellNum = cellNameCellNumMapping.get(cellName);
        Workbook workbook = null;
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(new File(filePath));
            //首先获取workbook对象
            workbook = WorkbookFactory.create(input);
            //拿到Sheet对象
            Sheet sheet = workbook.getSheet(sheetName);

            Cell cell = sheet.getRow(rowNum).getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(result);
            output = new FileOutputStream(new File(filePath));
            workbook.write(output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 讲结果封装为对象，保存在List中
     *
     * @param caseId
     * @param cellName
     * @param result
     */
    public static void saveWriteBackData(String caseId, String cellName, String result) {
        WriteBackData writeBackData = new WriteBackData();
        writeBackData.setCaseId(caseId);
        writeBackData.setCellName(cellName);
        writeBackData.setResult(result);
        writeBackDataList.add(writeBackData);
    }

    public static void main(String[] args) {

        for (Map.Entry<String, Integer> entry : caseIdRowNumMapping.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        for (Map.Entry<String, Integer> entry : cellNameCellNumMapping.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    /**
     * 批量写入数据
     *
     * @param filePath
     * @param sheetName
     */
    public static void batchWriteBackDatas(String filePath, String sheetName) {
        System.out.println("批量回写数据");
        Workbook workbook = null;
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(new File(filePath));
            //首先获取workbook对象
            workbook = WorkbookFactory.create(input);
            //拿到Sheet对象
            Sheet sheet = workbook.getSheet(sheetName);
            for (WriteBackData data : ExcelUtil.writeBackDataList) {
                String caseId = data.getCaseId();
                int rowNum = caseIdRowNumMapping.get(caseId);
                String cellName = data.getCellName();
                int cellNum = cellNameCellNumMapping.get(cellName);
                Cell cell = sheet.getRow(rowNum).getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(data.getResult());
            }
            output = new FileOutputStream(new File(filePath));
            workbook.write(output);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
