package com.ronhe.romp.schedule.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author
 * 
 */
public class PageBodyDto<T>  implements Serializable { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	* 当前页数
	*/
	private int pageNo = 1; 
	/**
	 * 每页显示个数
	 */
	private int pageSize = 10;// 每页显示记录
	private String sort = "";// 排序字段
	private List<T> results;// 结果
	private int total;//总记录数

	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	 
	public List<T> getResults() {
		return results;
	}
	public void setResults(List<T> results) {
		this.results = results;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	 
}
