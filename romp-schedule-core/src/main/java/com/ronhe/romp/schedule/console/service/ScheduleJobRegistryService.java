package com.ronhe.romp.schedule.console.service;

import java.util.List;

import com.ronhe.romp.schedule.console.model.ScheduleJobRegistryModel;
import com.ronhe.romp.schedule.console.service.jdbc.SQLDaoSupportService;

public interface ScheduleJobRegistryService extends SQLDaoSupportService<ScheduleJobRegistryModel>{
	
	public List<ScheduleJobRegistryModel> getJobDeadRegistryList(int timeOut);
	
	public List<ScheduleJobRegistryModel> getJobAliveRegistryList(int timeOut);

	public void registryDelete(String registryGroup, String registryKey, String registryValue);
}
