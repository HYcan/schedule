package com.ronhe.romp.schedule.console.service;

import java.util.List;

import com.ronhe.romp.schedule.console.model.ScheduleJobModel;
import com.ronhe.romp.schedule.console.service.jdbc.SQLDaoSupportService;

public interface ScheduleJobService extends SQLDaoSupportService<ScheduleJobModel>{
   
	public List<ScheduleJobModel>getJobsByGroup(String jobGroup);
}
