package com.ronhe.romp.schedule.console.util.validation.parser.impl;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ronhe.romp.schedule.console.util.validation.ValidateResult;
import com.ronhe.romp.schedule.console.util.validation.annotation.NumberLetter;
import com.ronhe.romp.schedule.console.util.validation.parser.IAnnotationParser;

public class NumberLetterParser implements IAnnotationParser{

	public ValidateResult validate(Field f, Object value) {
		ValidateResult result = new ValidateResult();
		 if(f.isAnnotationPresent(NumberLetter.class)){
			 NumberLetter number = f.getAnnotation(NumberLetter.class);
	            if(value != null ){
	            	 String message = number.message();
	            	 String reg = "^[0-9A-Za-z]{8,16}$";
	            	 Pattern pattern = Pattern.compile(reg);  
	            	 Matcher matcher = pattern.matcher(String.valueOf(value));  
	            	 if(message == null || message.toString().length()==0){
		            		message = "格式不符合要求";
		             }
	            	 boolean flag = matcher.matches();
	            	 if(!flag){
	            		 result.setValid(false);
	            		 result.setMessage(number.fieldName() + message);
	            	 }
	            }
	        }
		return result;
	}

}
