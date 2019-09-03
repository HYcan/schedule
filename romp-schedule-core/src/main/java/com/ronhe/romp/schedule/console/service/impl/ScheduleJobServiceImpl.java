package com.ronhe.romp.schedule.console.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ronhe.romp.schedule.console.model.ScheduleJobModel;
import com.ronhe.romp.schedule.console.service.ScheduleJobService;
import com.ronhe.romp.schedule.console.service.jdbc.SQLDaoSupportServiceImpl;
@Service
public class ScheduleJobServiceImpl extends SQLDaoSupportServiceImpl<ScheduleJobModel> implements ScheduleJobService{

	@Override
	public List<ScheduleJobModel> getJobsByGroup(String jobGroup) {
		ScheduleJobModel scheduleJobModel = new ScheduleJobModel();
		scheduleJobModel.setJobGroup(jobGroup);
		return  super.getListByCondtion(scheduleJobModel);
	}

}
