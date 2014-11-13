package com.task.activity.data;

import java.util.Date;

public class SdInforItem {

	
	private String SdTime;
	private String srcIp;
	private String dstIp;
	private String srcPort;
	private String dstPort;
	private String VideoType;
	
	public String getSdTime() {
		return SdTime;
	}

	public void setSdTime(String sdTime) {
		SdTime = sdTime;
	}

	public String getSrcIp() {
		return srcIp;
	}

	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}

	public String getDstIp() {
		return dstIp;
	}

	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}

	public String getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}

	public String getDstPort() {
		return dstPort;
	}

	public void setDstPort(String dstPort) {
		this.dstPort = dstPort;
	}

	public String getVideoType() {
		return VideoType;
	}

	public void setVideoType(String videoType) {
		VideoType = videoType;
	}

	
	
	
	
	
}
