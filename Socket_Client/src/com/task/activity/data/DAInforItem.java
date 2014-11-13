package com.task.activity.data;

public class DAInforItem {

	private int id;
	private String sn;
	private String ip;
	private int status;
	private double slotsused;
	private double cpuusage;
	private double memusage;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public double getSlotsused() {
		return slotsused;
	}
	public void setSlotsused(double slotsused) {
		this.slotsused = slotsused;
	}
	public double getCpuusage() {
		return cpuusage;
	}
	public void setCpuusage(double cpuusage) {
		this.cpuusage = cpuusage;
	}
	public double getMemusage() {
		return memusage;
	}
	public void setMemusage(double memusage) {
		this.memusage = memusage;
	}
	
	
}
