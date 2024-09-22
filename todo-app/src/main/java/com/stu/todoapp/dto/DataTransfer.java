package com.stu.todoapp.dto;

public class DataTransfer {

	private int ID;
	private String name;
	private String Task;
	private String Task_discription;
	private String date;
	
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTask() {
		return Task;
	}
	public void setTask(String task) {
		Task = task;
	}
	public String getTask_discription() {
		return Task_discription;
	}
	public void setTask_discription(String task_discription) {
		Task_discription = task_discription;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}	
}
