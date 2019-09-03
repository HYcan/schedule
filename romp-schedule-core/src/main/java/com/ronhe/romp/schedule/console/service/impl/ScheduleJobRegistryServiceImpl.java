package com.ronhe.romp.schedule.console.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ronhe.romp.schedule.console.model.ScheduleJobRegistryModel;
import com.ronhe.romp.schedule.console.service.ScheduleJobRegistryService;
import com.ronhe.romp.schedule.console.service.jdbc.SQLDaoSupportServiceImpl;
import com.ronhe.romp.schedule.console.util.DateFormateUtil;
@Service
public class ScheduleJobRegistryServiceImpl extends SQLDaoSupportServiceImpl<ScheduleJobRegistryModel> implements ScheduleJobRegistryService{

	@Override
	public List<ScheduleJobRegistryModel> getJobDeadRegistryList(int timeOut) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql="select * from schedule_job_registry where update_time <'"+DateFormateUtil.addSecondToDateTime(new Date(), -timeOut)+"'"; 
		return super.getListBySql(sql, params);
	}

	@Override
	public List<ScheduleJobRegistryModel> getJobAliveRegistryList(int timeOut) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql="select * from schedule_job_registry where update_time >'"+DateFormateUtil.addSecondToDateTime(new Date(), -timeOut)+"'"; 
		return super.getListBySql(sql, params);
	}

	@Override
	public void registryDelete(String registryGroup, String registryKey, String registryValue) {
		ScheduleJobRegistryModel jobRegistry = new ScheduleJobRegistryModel();
		jobRegistry.setRegistryGroup(registryGroup);
		jobRegistry.setRegistryKey(registryKey);
		jobRegistry.setRegistryValue(registryValue);
		super.delete(jobRegistry);
	}

}
