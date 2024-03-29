package com.ronhe.romp.schedule.console.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;

/**
 * @description
 */
public class ObjectUtil {
	/** 类型转换器集合 */
	public static final List<Converter<String, ? extends Object>> CONVERTERS = 
			new ArrayList<Converter<String, ? extends Object>>();

 

	/**
	 * 判断clazz是否是superClass或其子类
	 *
	 * @param clazz
	 * @param superClass
	 * @return
	 */
	public static boolean isExtends(Class<?> clazz, Class<?> superClass) {
		if(clazz == superClass) {
			return true;
		}
		Class<?> _class = clazz.getSuperclass();
		while (_class != null) {
			if(_class == superClass) {
				return true;
			}
			_class = _class.getSuperclass();
		}
		return false;
	}
	
	/**
	 * 判断clazz是否实现或继承interfaceClass
	 *
	 * @param clazz
	 * @param interfaceClass
	 * @return
	 */
	public static boolean isImplement(Class<?> clazz, Class<?> interfaceClass) {
		if(!interfaceClass.isInterface()) {
			return false;
		}
		if(clazz == interfaceClass) {
			return true;
		}
		List<Class<?>> interfaces = new ArrayList<Class<?>>();
		if(clazz.isInterface()) {
			interfaces.addAll(getSuperInterfaces(clazz));
		} else {
			interfaces.addAll(getClassInterfaces(clazz));
		}
		return interfaces.contains(interfaceClass);
	}
	
	/**
	 * @description 获得类的所有属性
 	 * @param clazz
	 */
	public static List<Field> getAllField(Class<?> clazz) {
		List<Field> list = new ArrayList<Field>();
		for (Field field : clazz.getDeclaredFields()) {
			list.add(field);
		}
		Class<?> _clazz = clazz.getSuperclass();
		while(_clazz != null) {
			list.addAll(getAllField(_clazz));
			_clazz = _clazz.getSuperclass();
		}
		return list;
	}
	
	/**
	 * 获取一个Class对象的所有字段名
	 * @return
	 * @Description:
	 * @return List<String>
	 */
	public static List<String> getAllFieldNames(Class<?> clazz){
		List<String> list = new ArrayList<String>();
		List<Field> fields = getAllDeclareFields(clazz);
		for (Field field : fields) {
			list.add(field.getName());
		}
		return list;
	}
	
	/**
	 * 获取当前类的所有字段
	 * @param clazz
	 * @return
	 * @Description:
	 * @return List<Field>
	 */
	public static List<Field> getAllDeclareFields(Class<?> clazz){
		List<Field> list = new ArrayList<Field>();
		for (Field field : clazz.getDeclaredFields()) {
			list.add(field);
		}
		return list;
	}
	
	/**
	 * 获得类的所有标注了指定的atClass类型注解的属性
	 *
	 * @param clazz
	 * @param atClass
	 * @return
	 */
	public static List<Field> getFieldsByAnnotation(Class<?> clazz, Class<? extends Annotation> atClass) {
		List<Field> list = getAllField(clazz);
		List<Field> ret = new ArrayList<Field>();
		for (Field field : list) {
			ret.add(field);
		}
		return list;
	}

	/**
	 * 获得类的属性和读方法的映射关系
	 *
	 * @param clazz
	 * @return Map<String, Method> key: 属性名，value： 读方法反射对象
	 */
	public static Map<String, Method> getReadMethodMapping(Class<?> clazz) {
		Map<String, Method> getLowerMapping = new HashMap<String, Method>();
		Map<String, Method> getUpperMapping = new HashMap<String, Method>();
		Map<String, Method> isLowerMapping = new HashMap<String, Method>();
		Map<String, Method> isUpperMapping = new HashMap<String, Method>();
		for (Method method : clazz.getMethods()) {
			if(method.getParameterTypes().length > 0) {
				continue;
			}
			String methodName = method.getName();
			if(method.getReturnType() == boolean.class && methodName.startsWith("is")) {
				if(methodName.matches("^is[a-z_].*$")) {
					isLowerMapping.put(methodName.substring(2), method);
				} else if(methodName.matches("^is[A-Z].*$")) {
					isUpperMapping.put(methodName.substring(2, 3).toLowerCase() + methodName.substring(3), method);
				}
			} else {
				if(methodName.matches("^get[a-z_].*$")) {
					getLowerMapping.put(methodName.substring(3), method);
				} else if(methodName.matches("^get[A-Z].*$")) {
					getUpperMapping.put(methodName.substring(3, 4).toLowerCase() + methodName.substring(4), method);
				}
			}
		}
		for (String field : getUpperMapping.keySet()) {
			getLowerMapping.put(field, getUpperMapping.get(field));
		}
		for (String field : isLowerMapping.keySet()) {
			getLowerMapping.put(field, isLowerMapping.get(field));
		}
		for (String field : isUpperMapping.keySet()) {
			getLowerMapping.put(field, isUpperMapping.get(field));
		}
		return getLowerMapping;
	}

	/**
	 * @description 将Map对象转换成clazz指定的类型对象
	 * @param map
	 * @param clazz
	 * @return Object
	 */
	public static <T> T toBean(Map<String, Object> map, Class<T> clazz) throws Exception{
		 T obj = clazz.newInstance();
	        // 获取javaBean的BeanInfo对象
	        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
	        // 获取属性描述器
	        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
	            // 获取属性名
	            String key = propertyDescriptor.getName();
	            Object value = map.get(key);
	            Method writeMethod = propertyDescriptor.getWriteMethod();
	            if (map.containsKey(key)){
	                // 解决 argument type mismatch 的问题，转换成对应的javaBean属性类型
	                String typeName = propertyDescriptor.getPropertyType().getTypeName();
	                // System.out.println(key +"<==>"+ typeName);
	                if ("java.lang.Integer".equals(typeName)){
	                    value = Integer.parseInt(value.toString());
	                }
	                if ("java.lang.Long".equals(typeName)){
	                    value = Long.parseLong(value.toString());
	                }
	                if ("java.util.Date".equals(typeName)){
	                    value = new SimpleDateFormat("yyyy-MM-dd").parse(value.toString());
	                }
	            }
	            // 通过反射来调用javaBean定义的setName()方法
	            writeMethod.invoke(obj,value);
	        }
	        return obj;
	}
	
	/**
	 * @description 序列化对象
	 * @param obj
	 * @return byte[]
	 */
	public static byte[] serialize(Object obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
		    oos.flush();
		    oos.close();
		    return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	    
	}

	/**
	 * @description 反序列化方法
	 * @param bytes
	 * @return Object
	 */
	public static Object unSerialize(byte[] bytes) {
		Object obj = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);	
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			obj = ois.readObject();
			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		 
	}
	
	/**
	 * @description 对象的深层复制，obj的实例类型必须实现序列化接口，否则将抛出异常
	 * @param obj
	 * @return T
	 */
	public static <T> T clone(T obj) {
		@SuppressWarnings("unchecked")
		T ret = (T) unSerialize(serialize(obj));
		return ret; 
	}

	/**
	 * 获得指定类型的转换器，不存在返回null
	 *
	 * @param type
	 * @return
	 * 修改说明：
	 */
	public static Converter<String, ? extends Object> getConverter(Class<? extends Object> type) {
		for (Converter<String, ? extends Object> converter : CONVERTERS) {
			ParameterizedType parameterizedType = 
				(ParameterizedType)converter.getClass().getGenericInterfaces()[0];
			Type[] params = parameterizedType.getActualTypeArguments();
			@SuppressWarnings("unchecked")
			Class<? extends Object> _type = (Class<? extends Object>)params[1];
			if(isExtends(type, _type)) {
				return converter;
			}
		}
		return null;
	}

	/**
	 * 将value值转换成type指定的类型
	 *
	 * @param <T>
	 * @param value
	 * @param type
	 * @return
	 * 修改说明：
	 */
	@SuppressWarnings("unchecked")
	public static <T> Object convert(Object value, Type type) throws Exception{
		Object _value = value;
		Class<? extends Object> _type = null;
		if(type instanceof Class) {
			_type = (Class<? extends Object>)type;
		} else {
			_type = (Class<? extends Object>)((ParameterizedType)type).getRawType();
		}
		if(_value != null) {
			if(!_type.isInstance(_value)) {
				if(getConverter(_type) != null) {
					_value = getConverter(_type).convert(_value.toString());
				} else if(_value instanceof Map) {
					_value = parse((Map<String, Object>)_value, _type);
				} else {
					try {
						_value = _type.getConstructor(String.class).newInstance(_value.toString());
					} catch (Exception e) {
					}
				}
			} else {
				if(_value instanceof List) {
					List<T> __value = new ArrayList<T>();
					Type itemType = null;
					try {
						itemType = ((ParameterizedType)type).getActualTypeArguments()[0];
					} catch (Exception e) {
						itemType = Object.class;
					}
					for (Object item : (List<Object>)_value) {	
						T t = (T) convert(item, itemType);
						__value.add(t);
					}
					_value = __value;
				}
			}
		}
		return _value;
	}

	/**
	 * 使用转换器将map对象解析成clazz指定类型的对象 
	 *
	 * @param <T>
	 * @param map     Map的value值类型只能是基本数据类型、String、List、Map
	 * 				    且List的元素值、子Map的value值类型取值范围同上。
	 * @param clazz
	 * @return
	 * 修改说明：
	 */
	public static <T> T parse(Map<String, Object> map, Class<T> clazz) throws Exception{
		Set<String> keys = map.keySet();
		List<Field> fields = getAllField(clazz);
		Map<String, Object> _map = new HashMap<String, Object>();
		Map<String, Object> __map = new HashMap<String, Object>();
		for (Field field : fields) {
			String fieldName = field.getName();
			if(!keys.contains(fieldName)) {
				continue;
			}
			try {
				new PropertyDescriptor(fieldName, clazz).getWriteMethod();
			} catch (Exception e) {
				continue;
			}
			Object value = map.get(fieldName);
			Type type = field.getGenericType();			
			Object _value = convert(value, type);
			if(_value == value || !field.getType().isInstance(_value)) {
				_map.put(fieldName, _value);
			} else {
				__map.put(fieldName, _value);
			}
		}
		T t = toBean(_map, clazz);
		for (String field : __map.keySet()) {
			try {
				new PropertyDescriptor(field, clazz)
						.getWriteMethod().invoke(t, __map.get(field));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return t;
	}
	
	/**
	 * 递归获取类的实现接口
	 *
	 * @param clazz 指定类类型
	 * @return
	 * 修改说明：
	 */
	private static List<Class<?>> getClassInterfaces(
			Class<?> clazz) {
		List<Class<?>> interfaces = new ArrayList<Class<?>>();
		for (Class<?> interface0 : clazz.getInterfaces()) {
			interfaces.addAll(getSuperInterfaces(interface0));
		}
		Class<?> class0 = clazz.getSuperclass();
		while (class0 != null) {
			interfaces.addAll(getClassInterfaces(class0));
			class0 = class0.getSuperclass();
		}
		return interfaces;
	}

	/**
	 * 递归获取指定接口及其继承的接口
	 *
	 * @param interface0 指定接口
	 * @return
	 * 修改说明：
	 */
	private static List<Class<?>> getSuperInterfaces(
			Class<?> interface0) {
		List<Class<?>> interfaces = new ArrayList<Class<?>>();
		interfaces.add(interface0);
		for (Class<?> interface1 : interface0.getInterfaces()) {
			interfaces.addAll(getSuperInterfaces(interface1));
		}
		return interfaces;
	}

}
