package com.lris.excel.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lris.excel.utils.annotation.ExcelColumn;

/**
 * Excel文件导入导出工具
 * @author CYX
 * @email chengyuxingo@gmail.com
 *
 * <br>2017年11月17日 下午5:50:51
 */
public class ExcelUtil {

	//--属性--//
	//private MultipartFile file;	//spring上传文件组件[保留]
	private InputStream in;
	private excelType fileType;
	private static final excelType EXCEL2003L = excelType.xls;
	private static final excelType EXCEL2010L = excelType.xlsx;
	private int sheetIndex = 0;		//默认读取第一个sheet
	private String sheetName = "Sheet1";	//sheet的默认名称
	private boolean headerExist = true;	//默认存在表头
	private int startRow = 0;	//默认第一行是表头
	private String datePattern = "yyyy-MM-dd";		//默认日期格式
	//--属性--//
	
	public ExcelUtil() {
		super();
	}

	/**
	 * @param file spring文件[保留]
	 */
	/*public ExcelUtil(MultipartFile file) {
		super();
		this.file = file;
	}*/
	
	/**
	 * Excel导入构造函数
	 * @param in
	 * @param fileType
	 */
	public ExcelUtil(InputStream in, excelType fileType) {
		super();
		this.in = in;
		this.fileType = fileType;
	}
	
	/**
	 * EXCEL文件类型
	 * @author CYX
	 * @email chengyuxingo@gmail.com
	 *
	 * <br>2017年11月22日 下午8:49:09
	 */
	public static enum excelType {
		xls,
		xlsx
	}
	
	private void checkNumberVaild() {
		if(startRow < 0 || sheetIndex < 0) {
			throw new IllegalArgumentException("参数值小于0");
		}
	}
	
	private Workbook workbook() {
		try {
			if(null != fileType) {
				if(EXCEL2003L == fileType) {
					return new HSSFWorkbook(in);
				}else if (EXCEL2010L == fileType) {
					return new XSSFWorkbook(in);
				}else {
					throw new Exception("文件类型错误！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//基于spring组件上传[保留]
	/*private Workbook workBook() {
		String fileType = "";
		try {
			if(null != file) {
				String fileName = file.getOriginalFilename();
				fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
				if(EXCEL2003L.equalsIgnoreCase(fileType)) {
					return new HSSFWorkbook(file.getInputStream());
				}else if (EXCEL2010L.equalsIgnoreCase(fileType)) {
					return new XSSFWorkbook(file.getInputStream());
				}else {
					throw new Exception("文件类型错误。");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

	/**
	 * 导入Excel文件
	 * <p>表头默认为第一行，如果包含表头，则从第一行开始读取数据，如果不包含表头，将从第二行开始读取数据</p>
	 * @return sheet数据
	 * <br>2017年11月17日下午10:52:24
	 */
	public List<List<Object>> importExcel(){
		checkNumberVaild();
		List<List<Object>> list = new ArrayList<>();
		List<Object> rowData = null;	//行数据
		try {
			Sheet sheet = workbook().getSheetAt(sheetIndex);
			if(null != sheet) {
				sheetName = sheet.getSheetName();
				if(!headerExist) {
					startRow += 1;
				}
				for(int i = startRow, j = sheet.getLastRowNum(); i <= j; i++) {
					Row row = sheet.getRow(i);
					if(null != row) {
						rowData = new ArrayList<>();
						for(int x = row.getFirstCellNum(), y = row.getLastCellNum(); x < y; x++) {
							if(null != row.getCell(x)) {
								rowData.add(getStringValue(row.getCell(x)));
							}else {
								rowData.add("");
							}
						}
						list.add(rowData);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 导入Excel
	 * <p>excel中存在表头，则调用此方法，默认第一行为表头</p>
	 * @param mapper 映射 <b>&lt;excel列名 , 自定义属性名&gt;</b>[忽略顺序]
	 * @return sheet数据
	 * <br>2017年11月18日上午11:09:45
	 * @throws Exception 
	 */
	public List<Map<String, Object>> importExcel(Map<String, String> mapper) throws Exception{
		checkNumberVaild();
		if(!headerExist) throw new IllegalArgumentException("当前headerExist属性为false");
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = null;
		try {
			Sheet sheet = workbook().getSheetAt(sheetIndex);
			if(null != sheet) {
				sheetName = sheet.getSheetName();
				Map<Integer, String> header = getHeader(sheet);
				for(int i = startRow + 1, j = sheet.getLastRowNum(); i <= j; i++) {
					Row row = sheet.getRow(i);
					if(null != row) {
						map = new HashMap<>();
						for(int x = row.getFirstCellNum(), y = mapper.size(); x < y; x++) {
							if(null != row.getCell(x)) {
								map.put(mapper.get(header.get(x)), getStringValue(row.getCell(x)));
							}else {
								map.put(mapper.get(header.get(x)), "");
							}
						}
						list.add(map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 导入Excel文件
	 * <p>excel中不存在表头，则调用此方法，默认从第一行开始读取</p>
	 * @param keys 自定义列的属性名 [建议顺序和excel列顺序相同]
	 * @return sheet数据
	 * <br>2017年11月18日上午11:19:00
	 */
	public List<Map<String, Object>> importExcel(String... keys){
		checkNumberVaild();
		if(headerExist) throw new IllegalArgumentException("当前HeaderExist为true");
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = null;
		try {
			Sheet sheet = workbook().getSheetAt(sheetIndex);
			if(null != sheet) {
				sheetName = sheet.getSheetName();
				for(int i = startRow, j = sheet.getLastRowNum(); i <= j; i++) {
					Row row = sheet.getRow(i);
					if(null != row) {
						map = new HashMap<>();
						for(int x = row.getFirstCellNum(), y = keys.length; x < y; x++) {
							if(null != row.getCell(x)) {
								map.put(keys[x], getStringValue(row.getCell(x)));
							}else {
								map.put(keys[x], "");
							}
						}
						list.add(map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 导入Excel文件
	 * <p>如果实体中使用了@ExcelColumn标记字段，则excel必须有表头，也就是startrow所在行为表头</p>
	 * <p>如果实体中没有使用@ExcelColumn标记任何字段,则默认excel没有表头，excel数据按实体字段顺序读取，建议表头顺序和字段顺序一致</p>
	 * <b>注意startrow的设置</b><br><br>
	 * @param clazz
	 * @return
	 * <br>2017年11月22日下午9:31:10
	 */
	public <T> List<T> importExcel(Class<T> clazz){
		checkNumberVaild();
		List<T> list = new ArrayList<>();
		try {
			Sheet sheet = workbook().getSheetAt(sheetIndex);
			if(null != sheet) {
				sheetName = sheet.getSheetName();
				Map<String, String> mapper = getMapper(clazz);
				if(mapper.isEmpty()) {
					Field[] fields = clazz.getDeclaredFields();
					for(int i = startRow, j = sheet.getLastRowNum(); i <= j; i++) {
						Row row = sheet.getRow(i);
						if(null != row) {
							T instance = clazz.newInstance();
							for(int x = row.getFirstCellNum(), y = fields.length; x < y; x++) {
								if(null != row.getCell(x)) {
									loadValue(clazz, instance, fields[x].getName(), getStringValue(row.getCell(x)));
								}else {
									continue;
								}
							}
							list.add(instance);
						}
					}
				}else {
					Map<Integer, String> header = getHeader(sheet);
					for(int i = startRow + 1, j = sheet.getLastRowNum(); i <= j; i++) {
						Row row = sheet.getRow(i);
						if(null != row) {
							T instance = clazz.newInstance();
							for(int x = row.getFirstCellNum(), y = row.getLastCellNum(); x < y; x++) {
								String headerValue = header.get(x);
								if(mapper.containsKey(headerValue)) {
									if(null != row.getCell(x)) {
										String key = mapper.get(headerValue);
										loadValue(clazz, instance, key, getStringValue(row.getCell(x)));
									}else {
										continue;
									}
								}else {
									continue;
								}
							}
							list.add(instance);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 导出excel文件[xlsx]
	 * @param data 数据
	 * @param headerNames excel列名
	 * @param HeaderFields 数据的属性名
	 * @param autoCellSetWidth 自动设置单元格宽度
	 * <p>如果为true，则单元格宽度随内容长度撑开，若为false，单元格宽度为缺省<p/>
	 * <p>列名和属性名一一对应，例：</p>
	 * <p>{"姓名","年龄","地址"}</p>
	 * <p>{"name","age","address"}</p>
	 * @return
	 * @throws IOException
	 * <br>2017年11月18日下午11:06:30
	 */
	public byte[] exportExcel(List<Map<String, Object>> data,String[] headerNames,String[] headerFields, boolean autoCellSetWidth) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			if(headerNames.length != headerFields.length) throw new IllegalAccessException("表头名和表头字段长度不同。");
			Sheet sheet = workbook.createSheet(sheetName);
			Row headerRow = sheet.createRow(0);
			for(int i = 0, j = headerNames.length; i < j; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headerNames[i]);
			}
			if(null != data) {
				if(autoCellSetWidth) {
					int[][] cellWidth = new int[data.size()][headerNames.length];
					for(int i = 0, j = data.size(); i < j; i++) {
						Row row = sheet.createRow(i + 1);
						for(int x = 0, y = headerNames.length; x < y; x++) {
							Cell cell = row.createCell(x);
							Object value = data.get(i).get(headerFields[x]);
							Object type = value.getClass();
							setValue(cell, type, value);
							cellWidth[i][x] = value.toString().getBytes().length + 2;
							if(i + 1 < j) {
								if((cellWidth[i + 1][x] < cellWidth[i][x]) && cellWidth[i][x] > 8 && cellWidth[i][x] < 100) {
									sheet.setColumnWidth(x, cellWidth[i][x] * 256);
								}
							}
						}
					}
				}else {
					for(int i = 0, j = data.size(); i < j; i++) {
						Row row = sheet.createRow(i + 1);
						for(int x = 0, y = headerNames.length; x < y; x++) {
							Cell cell = row.createCell(x);
							Object value = data.get(i).get(headerFields[x]);
							Object type = value.getClass();
							setValue(cell, type, value);
						}
					}
				}
			}
			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			workbook.close();
		}
		return outputStream.toByteArray();
	}
	
	/**
	 * 导出excel文件[xlsx]
	 * @param data 实体类数据
	 * <p>需配合@ExcelColumn注解</p>
	 * @param autoCellSetWidth 自动设置单元格宽度
	 * <p>如果为true，则单元格宽度随内容长度撑开，若为false，单元格宽度为缺省<p/>
	 * @return
	 * @throws IOException
	 * <br>2017年11月19日上午1:54:04
	 */
	public <T> byte[] exportExcel(List<T> data, boolean autoCellSetWidth) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			Map<String, String> mapper = getMapper(data.get(0).getClass());
			if(mapper.isEmpty()) throw new IllegalArgumentException("没有在" + data.get(0).getClass() + "上发现@ExcelColumn。");
			Object[] header = mapper.keySet().toArray(); 
			Object[] fields = mapper.values().toArray();
			Sheet sheet = workbook.createSheet(sheetName);
			Row headerRow = sheet.createRow(0);
			for(int i = 0, j = header.length; i < j; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(header[i].toString());
			}
			if(null != data) {
				if(autoCellSetWidth) {
					int[][] cellWidth = new int[data.size()][header.length];
					for(int i = 0, j = data.size(); i < j; i++) {
						T instance = data.get(i);
						Row row = sheet.createRow(i + 1);
						for(int x = 0, y = header.length; x < y; x++) {
							Cell cell = row.createCell(x);
							String getMethod = initGetMethod(fields[x].toString());
							Method method = instance.getClass().getDeclaredMethod(getMethod);
							Object value = method.invoke(instance) == null ? "" : method.invoke(instance);
							Object returnType = value.getClass();
							setValue(cell, returnType, value);
							cellWidth[i][x] = value.toString().getBytes().length + 2;
							if(i + 1 < j) {
								if((cellWidth[i + 1][x] < cellWidth[i][x]) && cellWidth[i][x] > 8 && cellWidth[i][x] < 100) {
									sheet.setColumnWidth(x, cellWidth[i][x] * 256);
								}
							}
						}
					}
				}else {
					for(int i = 0, j = data.size(); i < j; i++) {
						T instance = data.get(i);
						Row row = sheet.createRow(i + 1);
						for(int x = 0, y = header.length; x < y; x++) {
							Cell cell = row.createCell(x);
							String getMethod = initGetMethod(fields[x].toString());
							Method method = instance.getClass().getDeclaredMethod(getMethod);
							Object value = method.invoke(instance) == null ? "" : method.invoke(instance);
							Object returnType = value.getClass();
							setValue(cell, returnType, value);
						}
					}
				}
			}
			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			workbook.close();
		}
		return outputStream.toByteArray();
	}
	
	/**
	 * 将值注入到实体对象
	 * @param clazz 实体类
	 * @param instance 实例
	 * @param key 键名
	 * @param value 键值
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws ParseException
	 * @throws IllegalClassFormatException
	 * <br>2017年11月18日下午1:28:34
	 */
	private <T> void loadValue(Class<T> clazz, T instance, String key,String value) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException, IllegalClassFormatException {
		String getMethod = initGetMethod(key);
		Class<?> returnType = clazz.getDeclaredMethod(getMethod).getReturnType();
		Method method = clazz.getMethod(initSetMethod(key), returnType);
		if(returnType == String.class) {
			method.invoke(instance, value);
		}else if (returnType == int.class || returnType == Integer.class) {
			method.invoke(instance, Integer.parseInt(value));
		}else if (returnType == long.class || returnType == Long.class) {
			method.invoke(instance, Long.parseLong(value));
		}else if (returnType == float.class || returnType == Float.class) {
			method.invoke(instance, Float.parseFloat(value));
		}else if (returnType == double.class || returnType == Float.class) {
			method.invoke(instance, Double.parseDouble(value));
		}else if (returnType == Date.class) {
			method.invoke(instance, formatDate(value));
		}else {
			throw new IllegalClassFormatException("没有找到匹配的类型。");
		}
	}
	
	/**
	 * 装载值到单元格中
	 * @param cell
	 * @param returnType
	 * @param value
	 * <br>2017年11月19日上午1:44:14
	 */
	private void setValue(Cell cell, Object returnType, Object value) {
		if(returnType == Integer.class) {
			cell.setCellValue(Integer.parseInt(value.toString()));
		}else if (returnType == Long.class) {
			cell.setCellValue(Long.parseLong(value.toString()));
		}else if (returnType == Double.class) {
			cell.setCellValue(Double.parseDouble(value.toString()));
		}else if (returnType == Boolean.class) {
			cell.setCellValue(Boolean.parseBoolean(value.toString()));
		}else {
			cell.setCellValue(value.toString());
		}
	}
	
	/**
	 * 获取实体类对应excel映射
	 * @param clazz
	 * @return
	 * <br>2017年11月22日上午10:23:44
	 */
	public <T> Map<String, String> getMapper(Class<T> clazz){
		Map<String, String> map = new LinkedHashMap<>();
		Field[] fields = clazz.getDeclaredFields();
		for(byte i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			ExcelColumn excelColumn = fields[i].getAnnotation(ExcelColumn.class);
			if(excelColumn != null) {
				String value = excelColumn.value();
				if(!value.equals("")) {
					map.put(value, fieldName);
				}else {
					map.put(fieldName, fieldName);
				}
			}
		}
		return map;
	}
	
	/**
	 * Getter
	 * @param field
	 * @return
	 * <br>2017年11月18日下午1:35:51
	 */
	public String initGetMethod(String field) {
		return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}
	
	/**
	 * Setter
	 * @param field
	 * @return
	 * <br>2017年11月18日下午1:36:39
	 */
	public String initSetMethod(String field) {
		return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}
	
	/**
	 * 获取表头
	 * @param sheet
	 * @return
	 * <br>2017年11月18日下午1:36:57
	 */
	private Map<Integer, String> getHeader(Sheet sheet){
		Map<Integer, String> map = new HashMap<>();
		Row header = sheet.getRow(startRow);
		for(int i = header.getFirstCellNum(), j = header.getLastCellNum(); i < j; i++) {
			map.put(i, header.getCell(i).getStringCellValue().trim());
		}
		return map;
	}
	
	/**
	 * 将单元格数据转换为字符串
	 * @param cell
	 * @return
	 * <br>2017年11月18日下午1:37:20
	 */
	private String getStringValue(Cell cell) {
		String value = null;
		switch (cell.getCellTypeEnum()) {
		case STRING:
			value = cell.getStringCellValue();
			break;
		case BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case NUMERIC:
			if(HSSFDateUtil.isCellDateFormatted(cell)) {
				value = formatDate(cell.getDateCellValue());
			}else {
				value = String.valueOf((long)cell.getNumericCellValue());
			}
			break;
		case FORMULA:
			value = String.valueOf(cell.getCellFormula());
			break;
		case BLANK:
			value = "";
			break;
		case ERROR:
			value = "ERROR";
			break;
		default:
			break;
		}
		return value;
	}
	
	/**
	 * 格式化日期
	 * @param date
	 * @return
	 * <br>2017年11月18日下午1:38:10
	 */
	public String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		return sdf.format(date);
	}
	
	/**
	 * 格式化日期
	 * @param date
	 * @return
	 * @throws ParseException
	 * <br>2017年11月18日下午1:38:22
	 */
	public Date formatDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		return sdf.parse(date);
	}

	//Getter;Setter;
	
	public void setIn(InputStream in) {
		this.in = in;
	}

	public void setFileType(excelType fileType) {
		this.fileType = fileType;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setHeaderExist(boolean headerExist) {
		this.headerExist = headerExist;
	}
}
