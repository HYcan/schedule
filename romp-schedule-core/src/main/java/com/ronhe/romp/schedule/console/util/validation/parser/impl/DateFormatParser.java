package com.ronhe.romp.schedule.console.util.validation.parser.impl;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.ronhe.romp.schedule.console.util.validation.ValidateResult;
import com.ronhe.romp.schedule.console.util.validation.annotation.DateFormat;
import com.ronhe.romp.schedule.console.util.validation.parser.IAnnotationParser;

/**
 * 日期格式注解解析器
 * @author 
 *
 */
public class DateFormatParser implements IAnnotationParser{
	/**
     * 校验f字段的值是否符合value的日期格式
     */
    @Override
    public ValidateResult validate(Field f, Object value) {
        ValidateResult result = new ValidateResult();
        if(f.isAnnotationPresent(DateFormat.class)){
            DateFormat dateFormat = f.getAnnotation(DateFormat.class);
            try {
                if(value != null){
                    SimpleDateFormat format = new SimpleDateFormat(dateFormat.format());
                    format.parse(value.toString());
                }
            } catch (ParseException  e) {
            	result.setValid(false);
                result.setMessage(dateFormat.fieldName() + "不满足格式：" + dateFormat.format());
            }   
        }
        return result;
    }
}
