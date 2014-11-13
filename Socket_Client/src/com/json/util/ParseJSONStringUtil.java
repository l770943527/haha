package com.json.util;



import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import common.CommonHeader;

public class ParseJSONStringUtil {

	public static Gson gson;

	public static Gson getGson(){
		if(gson==null){
			GsonBuilder builder = new GsonBuilder();   
			builder.excludeFieldsWithoutExposeAnnotation();  
			gson = builder.create();  
		}
		return gson;
	}
	
	public static CommonHeader parseCommonHeader( String head )
	{
		CommonHeader commonheader = getGson().fromJson(head, CommonHeader.class);
		return commonheader; 
	}
	
	public static Map<String,Integer> parseMapResultData(String str){
		Map<String, Integer> map = (Map<String, Integer>) getGson().fromJson(str,   
                new TypeToken<Map<String, Integer>>() {   
             }.getType()); 
		return map;
	}
}
