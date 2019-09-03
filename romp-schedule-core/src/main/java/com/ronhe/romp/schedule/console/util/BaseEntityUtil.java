package com.ronhe.romp.schedule.console.util;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Id;

import com.ronhe.romp.schedule.console.util.annotation.CreateTime;
import com.ronhe.romp.schedule.console.util.annotation.Deleted;
import com.ronhe.romp.schedule.console.util.annotation.Max;
import com.ronhe.romp.schedule.console.util.annotation.UpdateTime;


public class BaseEntityUtil {

	
	/***
	 * 判断实体类主键是否为空
	 * 
	 * @param entity
	 *            实体类
	 * @return
	 * @throws Exception 
	 */
	public static boolean IsIdNull(Object entity) throws Exception {
		boolean isNull = false;
		Object value = ReflectUtil.getValue(entity, Id.class);
		if (value == null || "".equals(value) || value == new Integer(0)) {
			isNull = true;
		}  
		return isNull;

	}

	public static String getEntityColumnName(Object entity,String fieldName){
		if("".equals(fieldName)){
			return "";
		}
		String columnName = "";
		Field  fields [] = entity.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Column column = field.getAnnotation(Column.class);
			if(field.getName().equals(fieldName)){
				if(column!=null){
					columnName =column.name(); 
				}else{
					columnName =StringUtil.camelhumpToUnderline(fieldName);
				}
				break;
			}
		}
		return columnName;
	}
	
	
	public static String getEntityMaxField(Object entity){
		String fieldName = "";
		Field [] fs = entity.getClass().getDeclaredFields();
		for (Field field : fs) {
			field.setAccessible(true);
			Max max =  field.getAnnotation(Max.class);
			if(max!=null&&field.isAnnotationPresent(Max.class)){
				if(field.isAnnotationPresent(Column.class)){
					Column column =  field.getAnnotation(Column.class);
					fieldName = column.name();
				}
				if("".equals(fieldName)){
					fieldName = field.getName();
				}
				break;
			}
		}
		return fieldName;
	}
	
	public static void setEntityCreateTime(Object entity){
		ReflectUtil.setValue(entity, CreateTime.class,DateFormateUtil.getDateTime());
	} 
	public static void setEntityUpdateTime(Object entity){
		ReflectUtil.setValue(entity, UpdateTime.class,DateFormateUtil.getDateTime());
	} 
	
	public static void setEntityDeleted(Object entity,String value){
		ReflectUtil.setValue(entity, Deleted.class, value);
	}
	
	/**
	 * 设置对象的主键的值
	 * 
	 *  
	 */
	public static String setEntityId(Object entity){
		String uuid = UUIDGenerator.getUUID();
		ReflectUtil.setValue(entity, Id.class, uuid);
        return uuid;
	} 
	
	/**
	 * 设置对象的主键的值
	 * 
	 *  
	 */
	public static void setEntityId(Object entity,Object uuid){
		ReflectUtil.setValue(entity, Id.class, uuid);
	} 
	
	/**
	 * 获取实体主键的属性名
	 * @param entity
	 * @return
	 */
	public static String getEntityIdFieldName(Object entity){
        Field [] fs = entity.getClass().getDeclaredFields();
        Object value = "";
		for (Field field : fs) {
			field.setAccessible(true);
			Id id =  field.getAnnotation(Id.class);
			if(id!=null&&field.isAnnotationPresent(Id.class)){
				 value = field.getName();
			}
		}
		return String.valueOf(value);
	}
	
	/**
	 * 取得对象的主键的值
	 */
	public static String getEntityId(Object entity){
		Object value = ReflectUtil.getValue(entity, Id.class);
		if(value == null){
			 value = "";
		}
		return String.valueOf(value);
	}
 
	 
	/**
	 * 获取实体属性值
	 * @param entity
	 * @param fieldName
	 * @return
	 */
	public static Object getEntityField(Object entity,String fieldName){ 
		Object value= ReflectUtil.getValue(entity, fieldName);
		return value;
		
	}
	public static Object getSuperEntityField(Object entity,String fieldName){ 
		Object value= ReflectUtil.getValue(entity, fieldName);
		return value;
		
	}
	
}
