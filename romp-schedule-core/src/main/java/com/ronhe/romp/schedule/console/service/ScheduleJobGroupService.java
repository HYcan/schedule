package com.ronhe.romp.schedule.console.service;

import com.ronhe.romp.schedule.console.model.ScheduleJobGroupModel;
import com.ronhe.romp.schedule.console.service.jdbc.SQLDaoSupportService;

import java.util.List;

public interface ScheduleJobGroupService extends SQLDaoSupportService<ScheduleJobGroupModel>{

    public List<ScheduleJobGroupModel> findByAddressType(String addressType);
}
