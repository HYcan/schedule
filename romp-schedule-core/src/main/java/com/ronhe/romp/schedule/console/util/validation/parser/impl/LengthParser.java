package com.ronhe.romp.schedule.console.util.validation.parser.impl;

import java.lang.reflect.Field;

import com.ronhe.romp.schedule.console.util.validation.ValidateResult;
import com.ronhe.romp.schedule.console.util.validation.annotation.Length;
import com.ronhe.romp.schedule.console.util.validation.parser.IAnnotationParser;

public class LengthParser implements IAnnotationParser{

	public ValidateResult validate(Field f, Object value) {
		ValidateResult result = new ValidateResult();
		 if(f.isAnnotationPresent(Length.class)){
			 Length l= f.getAnnotation(Length.class);
	            if(value != null ){
	            	 int length = (String.valueOf(value)).length();
	            	 int max =l.max();
	            	 int min =l.min();
	            	 if(max>0&&length>max){
	            		 result.setValid(false);
	            		 result.setMessage(l.fieldName() + "长度不能大于"+max);
	            	 }
	            	 if(min>0&&length<min){
	            		 result.setValid(false);
	            		 result.setMessage(l.fieldName() + "长度不能小于"+min);
	            	 }
	            }
	        }
		return result;
	}

}
