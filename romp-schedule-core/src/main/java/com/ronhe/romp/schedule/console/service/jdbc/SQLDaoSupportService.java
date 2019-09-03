package com.ronhe.romp.schedule.console.service.jdbc;

import java.util.List;

import com.ronhe.romp.schedule.dto.PageBodyDto;
import com.ronhe.romp.schedule.dto.ResponseBodyDto;

public interface SQLDaoSupportService<T> {
	public  ResponseBodyDto save(T entity);
	public  ResponseBodyDto update(T entity) ;
	public  ResponseBodyDto saveOrUpdate(T entity);
	public  T getOne(T entity);
	public  T getById(Object id);
	public  void deleteById(String ids);
	public  void delete(T entity);
	public  void removeByIdInBatch(String ids);
	public  List<T> getListByCondtion(T entity);
	public  List<T> getAll();
	public  List<T> getListByInCondtion(T entity);
	public  void saveInBatch(List<T> entityList);
	public  PageBodyDto<T> getListPageByCondtion(T entity,PageBodyDto<T> pageBodyDtoMap);
}
