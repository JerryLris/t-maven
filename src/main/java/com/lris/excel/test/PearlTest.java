package com.lris.excel.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.lris.excel.po.Comments;
import com.lris.excel.po.Pearl;
import com.lris.excel.util.ExcelUtil;
import com.lris.excel.util.ExcelUtil.excelType;

public class PearlTest {

	@Test
	public void aaaTest() {
		System.out.println("dd");
	}

	@Test
	public void testPearOutTest() throws IOException {
		ExcelUtil excel = new ExcelUtil();
		excel.setDatePattern("yyyy-MM-dd HH:mm:ss");
		List<Pearl> list2 = new ArrayList<>();
		for(int i=0;i<10;i++) {
			Pearl pearl = new Pearl();
			pearl.setId(i+1);
			pearl.setF01(getInt());
			pearl.setF02(getInt());
			pearl.setF03(getInt());
			pearl.setF04(getInt());
			pearl.setF05(getInt());
			pearl.setF06(getInt());
			pearl.setF07(getInt());
			list2.add(pearl);
		}
		byte[] down = excel.exportExcel(list2,true);
		FileOutputStream outputStream = new FileOutputStream("F:\\Documents\\Desktop\\0514\\pearliii.xlsx");
		outputStream.write(down);
		outputStream.close();
	}
	@Test
	public void testPearIntTest() throws FileNotFoundException {
		ExcelUtil excel = new ExcelUtil(new FileInputStream("F:\\Documents\\Desktop\\0514\\222.xlsx"), excelType.xlsx);
		excel.setDatePattern("yyyy-MM-dd HH:mm:ss");
		excel.setStartRow(0);
		List<Pearl> list = excel.importExcel(Pearl.class);
		for (Pearl pearl : list) {
			System.out.println(pearl);
		}

	}
	public int getInt() {
		Random random = new Random();
		System.out.println(random.nextInt(10));
		return random.nextInt(10);
	}
}
