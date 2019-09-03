package com.ronhe.romp.schedule.console.util.validation.parser.impl;

import java.lang.reflect.Field;

import com.ronhe.romp.schedule.console.util.validation.ValidateResult;
import com.ronhe.romp.schedule.console.util.validation.annotation.NotBlank;
import com.ronhe.romp.schedule.console.util.validation.parser.IAnnotationParser;
/**
 * 不能为空白校验器
 *
 */
public class NotBlankParser implements IAnnotationParser{

	public ValidateResult validate(Field f, Object value) {
		 ValidateResult result = new ValidateResult();
		 if(f.isAnnotationPresent(NotBlank.class)){
	            NotBlank notBlank = f.getAnnotation(NotBlank.class);
	            if(value == null || value.toString().length() == 0){
	            	String message = notBlank.message();
	            	if(message == null || message.toString().length()==0){
	            		message = "不能为空";
	            	}
	            	result.setValid(false);
	                result.setMessage(notBlank.fieldName() + message);
	            }
	        }
		return result;
	}

}
