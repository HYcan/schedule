package com.ronhe.romp.schedule.console.service.jdbc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.ronhe.romp.schedule.console.util.BaseEntityUtil;
import com.ronhe.romp.schedule.console.util.DataConvertUtil;
import com.ronhe.romp.schedule.console.util.PageSqlUtil;
import com.ronhe.romp.schedule.console.util.builder.SimpleSqlBuilder;
import com.ronhe.romp.schedule.console.util.exception.JdbcException;
import com.ronhe.romp.schedule.dto.PageBodyDto;
import com.ronhe.romp.schedule.dto.ResponseBodyDto;

@Service
public class SQLDaoSupportServiceImpl<T> implements InitializingBean,SQLDaoSupportService<T> {
	private static final Logger log = LoggerFactory.getLogger(SQLDaoSupportServiceImpl.class);
	
	@Resource(name="scheduleJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	 
	
	/** 实体类类型 */
	private Class<T> entityClass;
	/** SQL语句构建对象 */
	private SimpleSqlBuilder<T> simpleSqlBuilder;

	/** SQL语句参数带名称的JDBC模版对象 */
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	public SQLDaoSupportServiceImpl() {
		Type genericSuperclass = getClass().getGenericSuperclass();
		if(genericSuperclass instanceof ParameterizedType){
			ParameterizedType parameterizedType= (ParameterizedType) genericSuperclass;
			Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
			this.entityClass = (Class<T>) actualTypeArguments[0];
		}else{
			this.entityClass= (Class<T>)genericSuperclass;
		}
		init(entityClass); 
	}

	public SQLDaoSupportServiceImpl(Class<T> entityClass) {
		init(entityClass);
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public void afterPropertiesSet() throws Exception {
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
	}

	/**
	 * 初始化方法
	 * 
	 * @param entityClass
	 * 实体类类型
	 */
	private void init(Class<T> entityClass) {
		this.entityClass = entityClass;
		// 初始化simpleSqlBuilder
		simpleSqlBuilder = new SimpleSqlBuilder<T>(entityClass);
	}
 
	/**
	 * 删除表全部数据
	 */
	public void deleteAll() {
		try {
			this.jdbcTemplate.update(simpleSqlBuilder.getDeleteAllSql());
		} catch (Exception e) {
			log.error(e.getMessage());
			
		}
	}
	
	public T getById(Object id) {
		try {
			String ids = id+",";
			String sql = simpleSqlBuilder.getListSql(ids.split(","));
        	List<Map<String, Object>> listMap = this.jdbcTemplate.queryForList(sql);
        	if(listMap!=null&&listMap.size()>0) {
        		return DataConvertUtil.mapToBean(listMap.get(0), entityClass);
        	}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException("根据主键查询对象出错！", e);
		}
		return null;

	}

	public void deleteById(String ids) {
		if (ids == null || "".equals(ids))
			return;
		try {
			jdbcTemplate.update(simpleSqlBuilder.getDeleteSql(),ids);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException("批量删除出错", e);
		}

	}
	
	public Integer getMax(T entity){
		try {
			String fieldName = BaseEntityUtil.getEntityMaxField(entity);
			String sql = simpleSqlBuilder.getSelectMaxSql(fieldName,null);
			Object o = this.jdbcTemplate.queryForObject(sql, Object.class);
			String v = o==null?"0":o.toString();
			return Integer.valueOf(v);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	
	public  PageBodyDto<T> getListPageByCondtion(T entity,PageBodyDto<T> pageBodyDtoMap) {
		List<T> list = new ArrayList<T>();
		try {
			String sql = simpleSqlBuilder.getSelectSql(DataConvertUtil.beanToMap(entity));
			List<Map<String, Object>> listMap = this.getListPageBySql(sql, pageBodyDtoMap.getPageNo(), pageBodyDtoMap.getPageSize(), DataConvertUtil.beanToMap(entity));
        	if(listMap!=null&&listMap.size()>0) {
        		for (Map<String, Object> map : listMap) {
					 list.add(DataConvertUtil.mapToBean(map, entityClass));
				}
        	}
			pageBodyDtoMap.setResults(list);
			pageBodyDtoMap.setTotal(listMap.size());
			return pageBodyDtoMap;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException("分页查询对象总数出错！", e);
		}
	}
	
	public Integer getMaxForWhere(T entity){
		try {
			String fieldName = BaseEntityUtil.getEntityMaxField(entity);
			Map<String, Object> batchArgs = simpleSqlBuilder.getRemoveSqlParameter(entity);
			String sql = simpleSqlBuilder.getSelectMaxSql(fieldName,batchArgs);
			Object o = this.jdbcTemplate.queryForObject(sql, Object.class);
			String v = o==null?"0":o.toString();
			return Integer.valueOf(v);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public void removeList(List<T> entityList) {
		if (entityList==null || entityList.size()==0) {
			return;
		}
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object>[] batchArgs = new Map[entityList.size()];
			for (int i = 0; i < batchArgs.length; i++) {
				T entity = entityList.get(i);
				BaseEntityUtil.setEntityDeleted(entity, "1");
				batchArgs[i] = simpleSqlBuilder.getAllSqlParameter(entity);
			}
			namedParameterJdbcTemplate.batchUpdate(simpleSqlBuilder.getRemoveSql(batchArgs), batchArgs);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	public void deleteById(List<String> idList) {
		if (idList == null || idList.isEmpty())
			return;
		try {
			String ids = "";
			StringBuffer sb = new StringBuffer();
			for (String id : idList) {
				sb.append(id).append(",");
			}
			ids = sb.toString();
			String id[] = ids.split(",");
			jdbcTemplate.execute(simpleSqlBuilder.getDeleteSql(id));
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException(e);
		}
	}
	
	public void delete(T entity) {
		try {
			String sql = simpleSqlBuilder.getDeleteSql(DataConvertUtil.beanToMap(entity));
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException("查询出错！", e);
		}
	}
	
	
	
	public  T getOne(T entity) {
		List<T> list = new ArrayList<T>();
		list = this.getListByCondtion(entity);
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	
	public  List<T> getAll(){
		  List<T> list = new ArrayList<T>();
		  try {
			    String sql =simpleSqlBuilder.getSelectAllSql();
				List<Map<String, Object>> listMap = this.jdbcTemplate.queryForList(sql);
				for (Map<String, Object> map : listMap) {
					 list.add(DataConvertUtil.mapToBean(map, entityClass));
				}
				return list;
		   } catch (Exception e) {
				log.error(e.getMessage());
				throw new JdbcException("查询出错！", e);
		  }
	}

	public List<T> getListByCondtion(T entity) {
		List<T> list = new ArrayList<T>();
		try {
			String sql = simpleSqlBuilder.getSelectSql(DataConvertUtil.beanToMap(entity));
			List<Map<String, Object>> listMap = this.jdbcTemplate.queryForList(sql);
			for (Map<String, Object> map : listMap) {
				  list.add(DataConvertUtil.mapToBean(map, entityClass));
			}
			return list;
					
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException("查询出错！", e);
		}
	}
	
	public List<T> getListByInCondtion(T entity) {
		List<T> list = new ArrayList<T>();
		try {
			String sql = simpleSqlBuilder.getSelectInSql(DataConvertUtil.beanToMap(entity));
			List<Map<String, Object>> listMap = this.jdbcTemplate.queryForList(sql);
			for (Map<String, Object> map : listMap) {
				  list.add(DataConvertUtil.mapToBean(map, entityClass));
			}
			return list;
					
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException("查询出错！", e);
		}
	}
	
	public List<T> getListBySql(String sql,Map<String, Object> params) {
		List<T> list = new ArrayList<T>();
		try {
			List<Map<String, Object>> listMap =  namedParameterJdbcTemplate.queryForList(sql, params);
			for (Map<String, Object> map : listMap) {
				  list.add(DataConvertUtil.mapToBean(map, entityClass));
			}
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return list;
	}
	
	public List<Map<String, Object>> getListPageBySql(String sql, int page, int rows,
			Map<String, Object> params) {
		try {
			sql = PageSqlUtil.createPageSql(jdbcTemplate,sql,  page,  rows);
			log.debug(sql);
			return namedParameterJdbcTemplate.queryForList(sql, params);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException(e);
		}
	}
	
	public void saveInBatch(List<T> entityList) {
		if (entityList == null || entityList.isEmpty())
			return;
		try {
			for (T entity : entityList) {
				BaseEntityUtil.setEntityDeleted(entity, "0");
				String uuid = BaseEntityUtil.getEntityId(entity);
				if("".equals(uuid)){
					BaseEntityUtil.setEntityId(entity);
				}
			}
			saveList(entityList);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public void saveOrUpdateList(List<T> entityList) {
		if (entityList == null || entityList.isEmpty()) {
			return;
		}
		List<T> saves = new ArrayList<T>();
		List<T> updates = new ArrayList<T>();
		try {
			for (T entity : entityList) {
				BaseEntityUtil.setEntityDeleted(entity, "0");
				if (BaseEntityUtil.IsIdNull(entity)) {
					BaseEntityUtil.setEntityId(entity);
					BaseEntityUtil.setEntityCreateTime(entity);
					saves.add(entity);
				} else {
					BaseEntityUtil.setEntityUpdateTime(entity);
					updates.add(entity);
				}
			}
			saveList(saves);
			updateList(updates);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException(e);
		}
	}
	
    public void removeByIdInBatch(String ids) {
    	try {
    		String sql = simpleSqlBuilder.getListSql(ids.split(","));
        	List<Map<String, Object>> removeListMap = this.jdbcTemplate.queryForList(sql);
        	List<T> removeList = new ArrayList<T>();
        	for (Map<String, Object> map : removeListMap) {
        		removeList.add(DataConvertUtil.mapToBean(map, entityClass));
			}
        	this.removeList(removeList);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException(e);
		}
    	
    }
    
    
    public  ResponseBodyDto update(T entity) {
    	ResponseBodyDto responseBodyDto = new ResponseBodyDto();
    	List<T> entityList = new ArrayList<T>();
		try {
			BaseEntityUtil.setEntityUpdateTime(entity);
	    	BaseEntityUtil.setEntityDeleted(entity, "0");
	    	entityList.add(entity);
	    	updateList(entityList);
			if(entityList!=null&&entityList.size()>0) {
		        	responseBodyDto.setObj(entityList.get(0));
		    }
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException(e);
		}
		
		return responseBodyDto;
    }
	
    public  ResponseBodyDto save(T entity) {
    	ResponseBodyDto responseBodyDto = new ResponseBodyDto();
    	List<T> entityList = new ArrayList<T>();
		try {
			BaseEntityUtil.setEntityId(entity);
			BaseEntityUtil.setEntityCreateTime(entity);
	    	BaseEntityUtil.setEntityDeleted(entity, "0");
	    	entityList.add(entity);
			saveList(entityList);
			if(entityList!=null&&entityList.size()>0) {
		        	responseBodyDto.setObj(entityList.get(0));
		    }
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException(e);
		}
		
		return responseBodyDto;
    }
    
	public ResponseBodyDto saveOrUpdate(T entity) {
		ResponseBodyDto responseBodyDto = new ResponseBodyDto();
		List<T> entityList = new ArrayList<T>();
		entityList.add(entity);
		this.saveOrUpdateList(entityList); 
        if(entityList!=null&&entityList.size()>0) {
        	responseBodyDto.setObj(entityList.get(0));
        }
		return responseBodyDto;
	}
	
	public void updateList(List<T> entityList) {
		if (entityList == null || entityList.isEmpty()) {
			return;
		}
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object>[] batchArgs = new Map[entityList.size()];
			for (int i = 0; i < batchArgs.length; i++) {
				T entity = entityList.get(i);
				batchArgs[i] = simpleSqlBuilder.getAllSqlParameter(entity);
			}
			namedParameterJdbcTemplate.batchUpdate(simpleSqlBuilder.getUpdateSql(batchArgs), batchArgs);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void saveList(List<T> entityList) {
		if (entityList == null || entityList.isEmpty()) {
			return;
		}
		try {
			Map<String, Object>[] batchArgs = new Map[entityList.size()];
			for (int i = 0; i < batchArgs.length; i++) {
				T entity = entityList.get(i);
				batchArgs[i] = simpleSqlBuilder.getAllSqlParameter(entity);
			}
			namedParameterJdbcTemplate.batchUpdate(simpleSqlBuilder.getInsertSql(), batchArgs);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new JdbcException(e);
		}
	}

}
