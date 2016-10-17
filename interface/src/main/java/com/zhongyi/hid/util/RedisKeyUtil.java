package com.zhongyi.hid.util;

public abstract class RedisKeyUtil {
	
	
	public static String bundles(){
		return "hid-bundles-zset";
	}
	
	
	public static String bundleMap(){
		return "hid-bundles-map";
	}
	
	public static String bundle(String uid){
		return uid;
	}
	

}
