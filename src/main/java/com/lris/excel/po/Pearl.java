package com.lris.excel.po;

import com.lris.excel.utils.annotation.ExcelColumn;

public class Pearl {

	
	private int id;
	@ExcelColumn
	private int term;
	@ExcelColumn
	private int F01;
	@ExcelColumn
	private int F02;
	@ExcelColumn
	private int F03;
	@ExcelColumn
	private int F04;
	@ExcelColumn
	private int F05;
	@ExcelColumn
	private int F06;
	@ExcelColumn
	private int F07;
	@ExcelColumn
	private String createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getF01() {
		return F01;
	}

	public void setF01(int f01) {
		F01 = f01;
	}

	public int getF02() {
		return F02;
	}

	public void setF02(int f02) {
		F02 = f02;
	}

	public int getF03() {
		return F03;
	}

	public void setF03(int f03) {
		F03 = f03;
	}

	public int getF04() {
		return F04;
	}

	public void setF04(int f04) {
		F04 = f04;
	}

	public int getF05() {
		return F05;
	}

	public void setF05(int f05) {
		F05 = f05;
	}

	public int getF06() {
		return F06;
	}

	public void setF06(int f06) {
		F06 = f06;
	}

	public int getF07() {
		return F07;
	}

	public void setF07(int f07) {
		F07 = f07;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	




	

	
	
	
}
