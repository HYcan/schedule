package com.ronhe.romp.schedule.console.util.validation.parser.impl;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ronhe.romp.schedule.console.util.validation.ValidateResult;
import com.ronhe.romp.schedule.console.util.validation.annotation.IP;
import com.ronhe.romp.schedule.console.util.validation.parser.IAnnotationParser;

public class IPParser implements IAnnotationParser{

	public ValidateResult validate(Field f, Object value) {
		ValidateResult result = new ValidateResult();
		 if(f.isAnnotationPresent(IP.class)){
			 IP ip = f.getAnnotation(IP.class);
	            if(value != null ){
	            	 String message = ip.message();
	            	 Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
	            	 Matcher matcher = pattern.matcher(String.valueOf(value));  
	            	 if(message == null || message.toString().length()==0){
		            		message = "格式不符合要求";
		             }
	            	 boolean flag = matcher.matches();
	            	 if(!flag){
	            		 result.setValid(false);
	            		 result.setMessage(ip.fieldName() + message);
	            	 }
	            }
	        }
		return result;
	}

}
