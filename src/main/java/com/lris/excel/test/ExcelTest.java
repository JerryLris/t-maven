package com.lris.excel.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.lris.excel.po.Comments;
import com.lris.excel.util.ExcelUtil;
import com.lris.excel.util.ExcelUtil.excelType;

public class ExcelTest {
	
	@Test
	public void myTest() throws Exception {
		ExcelUtil excel = new ExcelUtil(new FileInputStream("F:\\Documents\\Desktop\\0514\\comments.xlsx"), excelType.xlsx);
		excel.setDatePattern("yyyy-MM-dd HH:mm:ss");
		excel.setStartRow(0);
		List<Comments> list = excel.importExcel(Comments.class);
		for (Comments comments : list) {
			System.out.println(comments);
		}
	}
	
	@Test
	public void ExportTest() throws IOException {
		ExcelUtil excel = new ExcelUtil();
		excel.setDatePattern("yyyy-MM-dd HH:mm:ss");
		List<Comments> list2 = new ArrayList<>();
		for(int i = 0;i<100;i++) {
			Comments comments = new Comments();
			comments.setArticleId(i+1);
			comments.setDate(excel.formatDate(new Date()));
			comments.setEmail("chengyuxingooo@gmail.com");
			comments.setId(i);
			comments.setName("chengyuxing");
			comments.setSendTo("小红帽aaa");
			list2.add(comments);
		}
		byte[] down = excel.exportExcel(list2,true);
		FileOutputStream outputStream = new FileOutputStream("F:\\Documents\\Desktop\\0514\\comments-1.xlsx");
		outputStream.write(down);
		outputStream.close();
	}
}
