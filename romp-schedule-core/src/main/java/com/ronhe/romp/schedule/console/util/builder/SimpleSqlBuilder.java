package com.ronhe.romp.schedule.console.util.builder;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ronhe.romp.schedule.console.util.ObjectUtil;
import com.ronhe.romp.schedule.console.util.StringUtil;

/**
 * 单表增删改查的SQL语句创建类
 */
public class SimpleSqlBuilder<T> {
	/** 日志对象  */
	private static final Logger log = LoggerFactory.getLogger(SimpleSqlBuilder.class);
	/** 表名 */
	private String tableName;
	/** ID属性  */
	private String idField;
	/** 实体类属性和数据表字段的映射 */
	private Map<String, String> fieldColumnMapping;
	
	private Map<String, Map<String, String>> orderFieldColumnMapping;
	
	/** 实体类类型  */
	private Class<T> entityClass;

	/**
	 * 构造方法
	 */
	public SimpleSqlBuilder(Class<T> entityClass) {
		init(entityClass);
	}

	/**
	 * 初始化方法
	 *
	 * @param entityClass  实体类类型
	 */
	private void init(Class<T> entityClass) {
		this.entityClass = entityClass;
		if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            if (!table.name().equals("")) {
            	tableName = table.name();
            }
        }
		//初始化表名
		setTableName(tableName);
		List<Field> columnFields = ObjectUtil.getFieldsByAnnotation(this.entityClass, Transient.class);
		if(columnFields==null || columnFields.size()==0) {
			 return;
		}
		//初始化实体类属性和数据表字段的映射
		Map<String, String> mapping = new HashMap<String, String>();
		Map<String, Map<String, String>> orderMapping = new HashMap<String, Map<String, String>>();
		for (Field field  : columnFields) {
			if(field.isAnnotationPresent(Transient.class) || Modifier.isStatic(field.getModifiers())){
				continue;
			}
			 
			if(field.isAnnotationPresent(Id.class)){
				Id id = field.getAnnotation(Id.class);
				if(id!=null){
					setIdField(field.getName());
				}
			}
			Column column = field.getAnnotation(Column.class);
			String name = "";
			if(column!=null){
				name = column.name();
			}else{
				name = field.getName();
				name = StringUtil.camelhumpToUnderline(name);
			}
			mapping.put(field.getName(), name);		
			
			OrderBy orderBy = field.getAnnotation(OrderBy.class);
			String orderName="";
			if(orderBy!=null) {
				String sort= orderBy.value();
				orderName = name = field.getName();
				orderName = StringUtil.camelhumpToUnderline(orderName);
				Map<String, String> sortMapping = new HashMap<String, String>();
				sortMapping.put(orderName, sort);
				orderMapping.put(field.getName(), sortMapping);
			}
		}
		setOrderFieldColumnMapping(orderMapping);
		setFieldColumnMapping(mapping);
	}
	/**
	 * 获得表名
	 *
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}

	private void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public Map<String, Map<String, String>> getOrderFieldColumnMapping() {
		return orderFieldColumnMapping;
	}

	public void setOrderFieldColumnMapping(Map<String, Map<String, String>> orderFieldColumnMapping) {
		this.orderFieldColumnMapping = orderFieldColumnMapping;
	}

	/**
	 * 获得ID属性名
	 *
	 * @return
	 */
	public String getIdField() {
		return idField;
	}

	private void setIdField(String idField) {
		this.idField = idField;
	}

	/**
	 * 获得实体类属性和数据表字段的映射
	 *
	 * @return Map<String, String> FieldName -> ColumnName
	 */
	public Map<String, String> getFieldColumnMapping() {
		return fieldColumnMapping;
	}
	
	private void setFieldColumnMapping(Map<String, String> fieldColumnMapping) {
		this.fieldColumnMapping = fieldColumnMapping;
	}

	/**
	 * 获得实体类型
	 *
	 * @return
	 */	
	public Class<T> getEntityClass() {
		return entityClass;
	}

	/**
	 * 获得指定列的属性名
	 *
	 * @param column   列名
	 * @return String  属性名
	 */
	public String getColumnField(String column) {
		for (String field : fieldColumnMapping.keySet()) {
			if(fieldColumnMapping.get(field).equalsIgnoreCase(column)) {
				return field;
			}
		}
		return null;
	}
 
	/**
	 * 获得通过ID查询单条记录的SQL语句
	 * @return
	 */
	public String getQuerySimpleSql() {
		String sql = "SELECT * FROM " + tableName + " WHERE " + 
				fieldColumnMapping.get(idField) + " = ?";
		log.debug(sql);
		return sql;
	}
	
	/**
	 * 获得查询所有记录的SQL语句
	 * @return
	 */
	public String getQueryAllSql() {
		String sql = "SELECT * FROM " + tableName;
		log.debug(sql);
		return sql;
	}
	
	/**
	 * 获得查询总记录数的SQL语句
	 * @return
	 */
	public String getQueryCountSql() {
		String sql = "SELECT count(*) FROM " + tableName;
		log.debug(sql);
		return sql;
	}
	
	public String getDeleteAllSql() {
		String sql = "DELETE FROM " + tableName;
		log.debug(sql);
		return sql;
	}
	
	
	public String getOrderBySql(String sql) {
		StringBuffer bufSql = new StringBuffer(sql);
		for (Map.Entry<String, Map<String, String>> orderEntry : orderFieldColumnMapping.entrySet()) { 
			if(fieldColumnMapping.get(orderEntry.getKey())!=null&&!"".equals(fieldColumnMapping.get(orderEntry.getKey()))) {
				bufSql.append(" order by ");
				for (Map.Entry<String, String> orderFieldEntry :orderEntry.getValue().entrySet()) { 
					bufSql.append(orderFieldEntry.getKey()).append(" ").append(orderFieldEntry.getValue());
				}
			}
		}
		return bufSql.toString();
		
	}
	
	/**
	 * 获得通过ID删除一条记录的SQL语句
	 * @return
	 */
	public String getDeleteSql() {
		String sql = "DELETE FROM " + tableName + " WHERE " + fieldColumnMapping.get(idField) + " = ?";
		log.debug(sql);
		return sql;
	}
	/**
     * sql语句IN中的数据量不能超过1000条的解决办法
     * @param strArry 参数条件集合
     * @param columnName 列名
     * @return
     */
	 public static String getSqlByArray(String[] strArry,String columnName) {        
	        StringBuffer sql = new StringBuffer("");
	        if (strArry != null) {
	            sql.append(columnName).append (" IN ( ");
	            for (int i = 0; i < strArry.length; i++) {
	                sql.append("'").append(strArry[i] + "',");
	                if ((i + 1) % 1000 == 0 && (i + 1) < strArry.length) {
	                    sql.deleteCharAt(sql.length() - 1);
	                    sql.append(" ) OR ").append(columnName).append (" IN (");
	                }
	            }
	            sql.deleteCharAt(sql.length() - 1);
	            sql.append(" )");
	        }
	        return sql.toString();
	 }
    
	public  String getDeleteSql(String  [] ids) {
		String condtion =getSqlByArray(ids,fieldColumnMapping.get(idField));
		String sql = "DELETE FROM " + tableName + " WHERE " + condtion;
		log.debug(sql);
		return sql;
	}
	
	public  String getListSql(String [] ids) {
		String condtion =getSqlByArray(ids,fieldColumnMapping.get(idField));
		String sql = "SELECT * FROM " + tableName + " WHERE " + condtion;
		log.debug(sql);
		return sql;
	}
	
	
	/**
	 * 获得插入一条记录的SQL语句（不包含ID属性，ID值由数据库生成）
	 *
	 * @return
	 */
	public String getInsertSql() {
		return getInsertSql(fieldColumnMapping.keySet(), false);
	}

	/**
	 * 获得插入一条记录的SQL语句（不包含ID属性，ID值由数据库生成），指定需要插入的列
	 *
	 * @param fieldSet
	 * @return
	 */
	public String getInsertSql(Set<String> fieldSet) {
		return getInsertSql(fieldSet, false);
	}

	/**
	 * 获得插入一条记录的SQL语句
	 * 
	 * @param isIncludeIdField 是否包含ID属性
	 * @return
	 */
	private String getInsertSql(Set<String> fieldSet, boolean isIncludeIdField) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		sb.append("INSERT INTO ").append(tableName).append("(");
		for (String field : fieldSet) {
//			if(field.equals(idField) && !isIncludeIdField) {
//				continue;
//			}
			sb.append(fieldColumnMapping.get(field)).append(", ");
			sb2.append(":").append(field).append(", ");
		}
		sb.delete(sb.length()-2, sb.length());
		sb2.delete(sb2.length()-2, sb2.length());
		sb.append(") VALUES(");
		sb.append(sb2);
		sb.append(")");
		String sql = sb.toString();
		return sql;
	}
	
	public String getSelectMaxSql(String fieldName,Map<String, Object> argsMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT").append(" max("+fieldName+")").append(" AS ").append(fieldName).append(" FROM ").append(tableName);
		
		if(argsMap!=null&&argsMap.size()>0) {
			sb.append(" WHERE 1=1 ");
			for (Map.Entry<String, Object> whereEntry : argsMap.entrySet()) { 
				if(fieldColumnMapping.get(whereEntry.getKey())!=null&&!"".equals(fieldColumnMapping.get(whereEntry.getKey()))) {
					
					if(whereEntry.getValue()!=null&&whereEntry.getValue().toString().trim().length()>0) {
						sb.append(" and ");
						sb.append(fieldColumnMapping.get(whereEntry.getKey())).append(" ='" + whereEntry.getValue() + "'");
					}
				}
				
			}
		}
		
		String sql = sb.toString();
		return sql;
	}
	
	public String getDeleteSql(Map<String, Object> argsMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE").append(" FROM ").append(tableName);
		
		if(argsMap!=null&&argsMap.size()>0) {
			sb.append(" WHERE 1=1 ");
			for (Map.Entry<String, Object> whereEntry : argsMap.entrySet()) { 
				if(fieldColumnMapping.get(whereEntry.getKey())!=null&&!"".equals(fieldColumnMapping.get(whereEntry.getKey()))) {
					
					if(whereEntry.getValue()!=null&&whereEntry.getValue().toString().trim().length()>0) {
						sb.append(" and ");
						sb.append(fieldColumnMapping.get(whereEntry.getKey())).append(" ='" + whereEntry.getValue() + "'");
					}
				}
				
			}
		}
		
		String sql = sb.toString();
		return sql;
	}
	
	
	public String getSelectInSql(Map<String, Object> argsMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT").append(" * ").append(" FROM ").append(tableName);
		if(argsMap!=null&&argsMap.size()>0) {
			sb.append(" WHERE 1=1 ");
			for (Map.Entry<String, Object> whereEntry : argsMap.entrySet()) { 
				if(fieldColumnMapping.get(whereEntry.getKey())!=null&&!"".equals(fieldColumnMapping.get(whereEntry.getKey()))) {
					
					if(whereEntry.getValue()!=null&&whereEntry.getValue().toString().trim().length()>0) {
						sb.append(" and ");
						String condtion =getSqlByArray(whereEntry.getValue().toString().split(","),fieldColumnMapping.get(whereEntry.getKey()));
						sb.append(condtion);
					}
				}
				
			}
		}
		String sql = sb.toString();
		return sql;
	}
	
	
	
	public String getSelectAllSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT").append(" * ").append(" FROM ").append(tableName);
		String sql = sb.toString();
		return sql;
	}
	
	public String getSelectSql(Map<String, Object> argsMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT").append(" * ").append(" FROM ").append(tableName);
		
		if(argsMap!=null&&argsMap.size()>0) {
			sb.append(" WHERE 1=1 ");
			for (Map.Entry<String, Object> whereEntry : argsMap.entrySet()) { 
				if(fieldColumnMapping.get(whereEntry.getKey())!=null&&!"".equals(fieldColumnMapping.get(whereEntry.getKey()))) {
					
					if(whereEntry.getValue()!=null&&whereEntry.getValue().toString().trim().length()>0) {
						sb.append(" and ");
						sb.append(fieldColumnMapping.get(whereEntry.getKey())).append(" ='" + whereEntry.getValue() + "'");
					}
				}
				
			}
		}
		
		String sql = sb.toString();
		sql = getOrderBySql(sql);
		return sql;
	}
	

	/**
	 * 获得更新一条记录的SQL语句
	 *
	 * @return
	 */
	public String getUpdateSql(Map<String, Object>[] batchArgs) {
		Map<String, Object> mapArgs = new HashMap<String, Object>();
		for (Map<String, Object> map : batchArgs) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {  
				if(entry.getValue()!=null){
					mapArgs.put(entry.getKey(), entry.getValue());
				}
			} 
		}
		return getUpdateSql(fieldColumnMapping.keySet(),mapArgs);
	}
	
	public String getRemoveSql(Map<String, Object> drMap,Map<String, Object> argsMap){
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(tableName).append(" SET");
		for (Map.Entry<String, Object> setEntry : drMap.entrySet()) {  
			 sb.append(" ").append(setEntry.getKey()).append(" =" + setEntry.getValue() + "");
		}
		sb.append(" WHERE ");
		for (Map.Entry<String, Object> whereEntry : argsMap.entrySet()) {  
			sb.append(StringUtil.camelhumpToUnderline(whereEntry.getKey())).append(" ='" + whereEntry.getValue() + "',");
		}
		sb.deleteCharAt(sb.length()-1);
		String sql = sb.toString();
		return sql;
	}
	
	public String getRemoveSql(Map<String, Object>[] batchArgs) {
		Map<String, Object> mapArgs = new HashMap<String, Object>();
		for (Map<String, Object> map : batchArgs) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {  
				mapArgs.put(entry.getKey(), entry.getValue());
			} 
		}
		return getRemoveSql(fieldColumnMapping.keySet(),mapArgs);
	}
    
	public String getRemoveSql(Set<String> fieldSet,Map<String, Object> mapArgs) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(tableName).append(" SET");
		for (String field : fieldSet) {
			if(field.equalsIgnoreCase(idField)) {
				continue;
			}
			for (Map.Entry<String, Object> entry : mapArgs.entrySet()) {  
				 if(StringUtil.camelhumpToUnderline(entry.getKey()).equalsIgnoreCase(fieldColumnMapping.get(field))){
					 if(entry.getValue()!=null&&entry.getValue().toString().length()>0){
						 sb.append(" ").append(fieldColumnMapping.get(field)).append(" =:" + field + ",");
					 }
				 } 
			}
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(" WHERE ").append(fieldColumnMapping.get(idField)).append(" =:").append(idField);
		String sql = sb.toString();
		return sql;
	}
	
	/**
	 * 获得更新一条记录的SQL语句，指定需要更新的列
	 *
	 * @param fieldSet    需要更新的列集合
	 * @return
	 */
	public String getUpdateSql(Set<String> fieldSet,Map<String, Object> mapArgs) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(tableName).append(" SET");
		for (String field : fieldSet) {
			if(field.equalsIgnoreCase(idField)) {
				continue;
			}
			for (Map.Entry<String, Object> entry : mapArgs.entrySet()) {  
				 if(StringUtil.camelhumpToUnderline(entry.getKey()).equalsIgnoreCase(fieldColumnMapping.get(field))){
						 sb.append(" ").append(fieldColumnMapping.get(field)).append(" =:" + field + ",");
				 } 
			}
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(" WHERE ").append(fieldColumnMapping.get(idField)).append(" =:").append(idField);
		String sql = sb.toString();
		return sql;
	}
	
	/**
	 * 获得InsertSql和UpdateSql的参数Map对象，过滤注解约束的列
	 *
	 * @param entity 实体对象
	 * @return       Map对象
	 */
	public Map<String, Object> getSqlParameters(T entity) {
		return getSqlParameters(entity, true);
	}
	/**
	 * 获得 removeSql
	 * @param entity
	 * @return
	 */
	public Map<String, Object> getRemoveSqlParameter(T entity) {
		Map<String, Object> params = new HashMap<String, Object>();
		for (String field : fieldColumnMapping.keySet()) {
			try {
				Object value = new PropertyDescriptor(
						field, entityClass).getReadMethod().invoke(entity);
				if(value!=null&&value.toString().length()>0){
					params.put(field, value);
				}
			} catch (Exception e) {
				throw new RuntimeException("getSqlParameters exception!", e);
			}
		}
		log.debug(params.toString());
		return params;
	}
	
	/**
	 * 获得InsertSql和UpdateSql的参数Map对象，不过滤注解约束的列
	 * @param entity
	 * @return
	 */
	public Map<String, Object> getAllSqlParameter(T entity) {
		return getSqlParameters(entity, false);
	}

	/**
	 * 获得InsertSql和UpdateSql的参数Map对象
	 * @param entity    实体参象
	 * @param isMarker  标记是否是新增或修改，true为新增
	 * @return
	 */
	private Map<String, Object> getSqlParameters(T entity, boolean isMarker) {
		Map<String, Object> params = new HashMap<String, Object>();
		for (String field : fieldColumnMapping.keySet()) {
			try {
				Object value = new PropertyDescriptor(
						field, entityClass).getReadMethod().invoke(entity);
					if(value == null) {
						params.put(field, null);
					}else{
						params.put(field, value);
					}
				 
			} catch (Exception e) {
				throw new RuntimeException("getSqlParameters exception!", e);
			}
		}
		return params;
	}


 
}
