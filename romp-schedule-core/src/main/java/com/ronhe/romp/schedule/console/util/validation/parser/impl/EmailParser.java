package com.ronhe.romp.schedule.console.util.validation.parser.impl;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ronhe.romp.schedule.console.util.validation.ValidateResult;
import com.ronhe.romp.schedule.console.util.validation.annotation.Email;
import com.ronhe.romp.schedule.console.util.validation.parser.IAnnotationParser;

public class EmailParser implements IAnnotationParser{

	public ValidateResult validate(Field f, Object value) {
		ValidateResult result = new ValidateResult();
		 if(f.isAnnotationPresent(Email.class)){
			    Email email = f.getAnnotation(Email.class);
	            if(value != null ){
	            	 String message = email.message();
	            	 String reg = "\\w+@(\\w+\\.){1,3}\\w+";
	            	 Pattern pattern = Pattern.compile(reg);  
	            	 Matcher matcher = pattern.matcher(String.valueOf(value));  
	            	 if(message == null || message.toString().length()==0){
		            		message = "格式不符合要求";
		             }
	            	 boolean flag = matcher.matches();
	            	 if(!flag){
	            		 result.setValid(false);
	            		 result.setMessage(email.fieldName() + message);
	            	 }
	            }
	        }
		return result;
	}

}
