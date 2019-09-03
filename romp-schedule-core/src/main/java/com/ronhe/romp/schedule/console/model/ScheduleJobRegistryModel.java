package com.ronhe.romp.schedule.console.model;

import javax.persistence.Id;
import javax.persistence.Table;

import com.ronhe.romp.schedule.console.util.annotation.UpdateTime;

@Table(name = "schedule_job_registry")
public class ScheduleJobRegistryModel {
    @Id
	private String id;
    private String registryGroup;
    private String registryKey;
    private String registryValue;
    @UpdateTime
    private String updateTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRegistryGroup() {
		return registryGroup;
	}
	public void setRegistryGroup(String registryGroup) {
		this.registryGroup = registryGroup;
	}
	public String getRegistryKey() {
		return registryKey;
	}
	public void setRegistryKey(String registryKey) {
		this.registryKey = registryKey;
	}
	public String getRegistryValue() {
		return registryValue;
	}
	public void setRegistryValue(String registryValue) {
		this.registryValue = registryValue;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
    
    
}
