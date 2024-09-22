package com.stu.studentapp.dto;

import java.io.Serializable;
import java.util.Objects;

public class Student implements Serializable {
	private int id;
	private String name;
	private double marks;
	private String emailId;
	private String password;
	private int row_number;
	private String date;
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
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
	public double getMarks() {
		return marks;
	}
	public void setMarks(double marks) {
		this.marks = marks;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRow_number() {
		return row_number;
	}

	public void setRow_number(int row_number) {
		this.row_number = row_number;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id,name,marks,emailId,password);
	}
}

