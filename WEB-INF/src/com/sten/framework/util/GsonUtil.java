package com.sten.framework.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by linkai on 2017/3/30.
 * 
 * 在属性上增加注解 @SerializedName("_parentId") Controller 返回 String 类型 字符串,
 * GsonUtil.toDateJson(list)
 */
public class GsonUtil {

	public static String toDateJson(Object obj) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return gson.toJson(obj);
	}

	public static String toDateTimeJson(Object obj) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();
		return gson.toJson(obj);
	}
}
