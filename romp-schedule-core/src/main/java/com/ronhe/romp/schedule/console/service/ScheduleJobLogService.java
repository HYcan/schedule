package com.ronhe.romp.schedule.console.service;

import com.ronhe.romp.schedule.console.model.ScheduleJobLogModel;
import com.ronhe.romp.schedule.console.service.jdbc.SQLDaoSupportService;

import java.util.List;

public interface ScheduleJobLogService extends SQLDaoSupportService<ScheduleJobLogModel>{

    public List<ScheduleJobLogModel> findFailJobLogIds(int pagesize);

    public int updateAlarmStatus(String jobLogId, int oldAlarmStatus, int newAlarmStatus);
}
