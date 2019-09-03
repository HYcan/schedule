package com.ronhe.romp.schedule.console.model;

import javax.persistence.Id;
import javax.persistence.Table;

import com.ronhe.romp.schedule.console.util.annotation.CreateTime;

import java.util.List;

@Table(name = "schedule_job_group")
public class ScheduleJobGroupModel {
    @Id
	private String id;
    private String appName;
    private String title;
    @CreateTime
    private String createTime;
    private String addressType="0";
    private String addressList;

	private String order;
	private List<String> registryList;  // 执行器地址列表(系统注册)

    public String getOrder() {
        return order;
    }
    public void setOrder(String order) {
        this.order = order;
    }
    public List<String> getRegistryList() {
        return registryList;
    }
    public void setRegistryList(List<String> registryList) {
        this.registryList = registryList;
    }

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getAddressList() {
		return addressList;
	}
	public void setAddressList(String addressList) {
		this.addressList = addressList;
	}
    
    
}
