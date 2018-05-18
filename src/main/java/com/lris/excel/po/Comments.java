package com.lris.excel.po;

import com.lris.excel.utils.annotation.ExcelColumn;

public class Comments {
	
	@ExcelColumn("编号")
	private int id;
	
	private String name;
	
	@ExcelColumn("邮箱")	
	private String email;
	
	@ExcelColumn("日期")	
	private String date;
	
	@ExcelColumn("收件人")	
	private String sendTo;
	
	@ExcelColumn("文章编号")	
	private int articleId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	@Override
	public String toString() {
		return "Comments [id=" + id + ", name=" + name + ", email=" + email + ", date=" + date + ", sendTo=" + sendTo
				+ ", articleId=" + articleId + "]";
	}
	
	





	
}
