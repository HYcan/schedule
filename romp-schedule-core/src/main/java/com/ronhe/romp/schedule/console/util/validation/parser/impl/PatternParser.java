package com.ronhe.romp.schedule.console.util.validation.parser.impl;

import java.lang.reflect.Field;
import java.util.regex.Matcher;

import com.ronhe.romp.schedule.console.util.validation.ValidateResult;
import com.ronhe.romp.schedule.console.util.validation.annotation.Pattern;
import com.ronhe.romp.schedule.console.util.validation.parser.IAnnotationParser;

public class PatternParser implements IAnnotationParser{


	public ValidateResult validate(Field f, Object value) {
		ValidateResult result = new ValidateResult();
		 if(f.isAnnotationPresent(Pattern.class)){
			 Pattern p = f.getAnnotation(Pattern.class);
	            if(value != null ){
	            	 String message = p.message();
		           	 String reg = p.regexp();
		           	 java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(reg);  
	            	 Matcher matcher = pattern.matcher(String.valueOf(value));  
	            	 if(message == null || message.toString().length()==0){
		            		message = "格式不符合要求";
		             }
	            	 boolean flag = matcher.matches();
	            	 if(!flag){
	            		 result.setValid(false);
	            		 result.setMessage(p.fieldName() + message);
	            	 }
	            }
	        }
		return result;
	}


}
