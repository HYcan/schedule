package com.ronhe.romp.schedule.console.util.validation.parser.impl;

import java.lang.reflect.Field;

import com.ronhe.romp.schedule.console.util.validation.ValidateResult;
import com.ronhe.romp.schedule.console.util.validation.annotation.Min;
import com.ronhe.romp.schedule.console.util.validation.parser.IAnnotationParser;

public class MinParser implements IAnnotationParser{

	public ValidateResult validate(Field f, Object value) {
		 ValidateResult result = new ValidateResult();
		 if(f.isAnnotationPresent(Min.class)){
			 Min min= f.getAnnotation(Min.class);
	            if(value != null ){
	            	 int m =  min.value();
	            	 if(m>0&&Integer.parseInt(String.valueOf(value))<m){
	            		 result.setValid(false);
	            		 result.setMessage(min.fieldName() + "不能小于"+m);
	            	 }
	            }
	        }
		return result;
	}

}
