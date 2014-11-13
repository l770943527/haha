package com.task.activity.data;

public class TimeValue {

	private String time;
	private int value;
	
	public TimeValue(String time, int value){
		this.time = time;
		this.value = value;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
}
