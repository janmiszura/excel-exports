package com.gleidsonfersanp.extra;

public class Util {

	public static Boolean isNullOrEmpty(String text){
		if(text == null || "".equals(text))
			return true;
		return false;
	}

}
