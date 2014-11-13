package com.json.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EncapSendInforUtil {

	public static Gson gson;

	public static Gson getGson() {
		if (gson == null) {
			GsonBuilder builder = new GsonBuilder();
			builder.excludeFieldsWithoutExposeAnnotation();
			gson = builder.create();
		}
		return gson;
	}

	public static String encapCloseConnectionReq() {

		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type", 20000);
		headDetail.put("stream_num", 0);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}
	
	public static String encapLoginInfor(String username, String password) {

		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type", 10000);
		headDetail.put("stream_num", 0);

		Map<String, String> dataDetail = new HashMap<String, String>();
		dataDetail.put("username", username);
		dataDetail.put("password", password);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}

	public static String encapDataAnalystStatusReq(int startPos, int len) {

		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = builder.create();

		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type", 11000);
		headDetail.put("stream_num", 0);

		Map<String, Integer> dataDetail = new HashMap<String, Integer>();
		dataDetail.put("startPos", startPos);
		dataDetail.put("len", len);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}

	public static String encapLogImforReq(int startPos, int len) {

		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type", 12000);
		headDetail.put("stream_num", 0);

		Map<String, Integer> dataDetail = new HashMap<String, Integer>();
		dataDetail.put("startPos", startPos);
		dataDetail.put("len", len);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}
	//add sd which are the parameters needed??
	public static String encapSdImforReq(int startPos, int len,int taskid) {

		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type",13400);
		headDetail.put("stream_num", 0);

		Map<String, Integer> dataDetail = new HashMap<String, Integer>();
		dataDetail.put("startPos", startPos);
		dataDetail.put("len", len);
		dataDetail.put("taskid",taskid);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}

	public static String encapRTTResultImforReq(int taskid,long timeMin, long timeMax,int type) {

		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type", 13000);
		headDetail.put("stream_num", 0);

		Map<String, Long> dataDetail = new HashMap<String, Long>();
		dataDetail.put("taskid", (long) taskid);
		dataDetail.put("timeMin", timeMin);
		dataDetail.put("timeMax", timeMax);
		dataDetail.put("type", (long) type);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}

	public static String encapIPTVReqResultImforReq(int taskid, long timeMin, long timeMax, int type) {
		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type", 13100);
		headDetail.put("stream_num", 0);

		Map<String, Long> dataDetail = new HashMap<String, Long>();
		dataDetail.put("taskid", (long) taskid);
		dataDetail.put("timeMin", timeMin);
		dataDetail.put("timeMax", timeMax);
		dataDetail.put("type", (long) type);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}
	
	public static String encapIPTVMDIResultImforReq(int taskid, long timeMin, long timeMax, int type,int level) {
		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type", 13200);
		headDetail.put("stream_num", 0);

		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date xMinDate = new Date(timeMin);
		Date xMaxDate = new Date(timeMax);
		String xMin = sdf.format(xMinDate);
		String xMax = sdf.format(xMaxDate);

		Map<String, String> dataDetail = new HashMap<String, String>();
		dataDetail.put("taskid",""+ taskid);
		dataDetail.put("timeMin", xMin);
		dataDetail.put("timeMax", xMax);
		dataDetail.put("type", ""+ type);
		dataDetail.put("level", ""+level);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}
	
	
	public static String encapTaskImforReq(int startPos, int len) {

		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type", 14000);
		headDetail.put("stream_num", 0);

		Map<String, Integer> dataDetail = new HashMap<String, Integer>();
		dataDetail.put("startPos", startPos);
		dataDetail.put("len", len);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}
	public static String encapTaskSearchImforReq(int startPos, int len,int taskid) {

		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type", 16000);
		headDetail.put("stream_num", 0);

		Map<String, Integer> dataDetail = new HashMap<String, Integer>();
		dataDetail.put("startPos", startPos);
		dataDetail.put("len", len);
		dataDetail.put("taskid",taskid);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}
	
	//added sw encap 
	public static String encapSwImforReq(int startPos, int len,int taskid) {

		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type",13300);
		headDetail.put("stream_num", 0);

		Map<String, Integer> dataDetail = new HashMap<String, Integer>();
		dataDetail.put("startPos", startPos);
		dataDetail.put("len", len);
		dataDetail.put("taskid",taskid);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}
	//avbw encap
	public static String encapAvbwImforReq(int startPos, int len,int taskid) {

		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type",17000);
		headDetail.put("stream_num", 0);

		Map<String, Integer> dataDetail = new HashMap<String, Integer>();
		dataDetail.put("startPos", startPos);
		dataDetail.put("len", len);
		dataDetail.put("taskid",taskid);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}
	
	public static String encapBobwImforReq(int startPos, int len,int taskid) {

		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type",18000);
		headDetail.put("stream_num", 0);

		Map<String, Integer> dataDetail = new HashMap<String, Integer>();
		dataDetail.put("startPos", startPos);
		dataDetail.put("len", len);
		dataDetail.put("taskid",taskid);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}


	public static String encapAlarmImforReq(int startPos, int len) {
		Map<String, Integer> headDetail = new HashMap<String, Integer>();
		headDetail.put("protocol_version", 1);
		headDetail.put("flag", 0);
		headDetail.put("message_type", 15000);
		headDetail.put("stream_num", 0);

		Map<String, Integer> dataDetail = new HashMap<String, Integer>();
		dataDetail.put("startPos", startPos);
		dataDetail.put("len", len);

		Map<String, Map> finalProtocalMap = new HashMap<String, Map>();
		finalProtocalMap.put("head", headDetail);
		finalProtocalMap.put("data", dataDetail);

		String mapToJson = getGson().toJson(finalProtocalMap);
		return mapToJson;
	}



	

	
	
}
