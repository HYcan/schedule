package com.ronhe.romp.schedule.console.util.validation.parser.impl;

import java.lang.reflect.Field;

import com.ronhe.romp.schedule.console.util.validation.ValidateResult;
import com.ronhe.romp.schedule.console.util.validation.annotation.Max;
import com.ronhe.romp.schedule.console.util.validation.parser.IAnnotationParser;

public class MaxParser implements IAnnotationParser{

	public ValidateResult validate(Field f, Object value) {
		 ValidateResult result = new ValidateResult();
		 if(f.isAnnotationPresent(Max.class)){
			 Max max= f.getAnnotation(Max.class);
	            if(value != null ){
	            	 int m =  max.value();
	            	 if(m>0&&Integer.parseInt(String.valueOf(value))>m){
	            		 result.setValid(false);
	            		 result.setMessage(max.fieldName() + "不能大于"+m);
	            	 }
	            }
	        }
		return result;
	}

}
