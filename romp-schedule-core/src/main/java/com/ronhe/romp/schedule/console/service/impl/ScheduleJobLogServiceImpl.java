package com.ronhe.romp.schedule.console.service.impl;

import com.ronhe.romp.schedule.console.model.ScheduleJobLogModel;
import com.ronhe.romp.schedule.console.service.ScheduleJobLogService;
import com.ronhe.romp.schedule.console.service.ScheduleJobService;
import com.ronhe.romp.schedule.console.service.jdbc.SQLDaoSupportServiceImpl;
import com.ronhe.romp.schedule.console.util.DateFormateUtil;
import com.ronhe.romp.schedule.dto.ResponseBodyDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleJobLogServiceImpl extends SQLDaoSupportServiceImpl<ScheduleJobLogModel> implements ScheduleJobLogService {

    @Override
    public List<ScheduleJobLogModel> findFailJobLogIds(int pagesize) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql="select * from schedule_job_log where !((trigger_code in (0, 200) and handle_code = 0) or (handle_code = 200))and alarm_status = 0 order by id ASC";
        return super.getListBySql(sql, params);
    }

    @Override
    public int updateAlarmStatus(String jobLogId, int oldAlarmStatus, int newAlarmStatus) {
        ScheduleJobLogModel query = new ScheduleJobLogModel();
        query.setId(jobLogId);
        query.setAlarmStatus(oldAlarmStatus);
        List<ScheduleJobLogModel> logList = super.getListByCondtion(query);
        if(logList != null && logList.size() > 0){
            ScheduleJobLogModel jobLog = logList.get(0);
            jobLog.setAlarmStatus(newAlarmStatus);
            super.update(jobLog);
            return 1;
        }
        return 0;
    }

}
