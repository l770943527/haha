package com.task.activity.data;

import java.util.Date;

public class AlarmInforItem {

	private int alarmId;
	private int cellId;
	private String alarmTime;
	private String alarmType;
	private String alarmIp;
	private int alarmInterface;
	private int taskId;
	private String description;
	private int alarmLevel;
	
	public int getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}
	public int getCellId() {
		return cellId;
	}
	public void setCellId(int cellId) {
		this.cellId = cellId;
	}
	public String getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public String getAlarmIp() {
		return alarmIp;
	}
	public void setAlarmIp(String alarmIp) {
		this.alarmIp = alarmIp;
	}
	public int getAlarmInterface() {
		return alarmInterface;
	}
	public void setAlarmInterface(int alarmInterface) {
		this.alarmInterface = alarmInterface;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getAlarmLevel() {
		return alarmLevel;
	}
	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
}
