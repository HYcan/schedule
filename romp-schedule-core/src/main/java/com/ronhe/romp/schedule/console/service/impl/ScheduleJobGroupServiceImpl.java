package com.ronhe.romp.schedule.console.service.impl;

import com.ronhe.romp.schedule.core.base.util.DateUtil;
import org.springframework.stereotype.Service;

import com.ronhe.romp.schedule.console.model.ScheduleJobGroupModel;
import com.ronhe.romp.schedule.console.service.ScheduleJobGroupService;
import com.ronhe.romp.schedule.console.service.jdbc.SQLDaoSupportServiceImpl;

import java.util.Date;
import java.util.List;

@Service("scheduleJobGroupService")
public class ScheduleJobGroupServiceImpl extends SQLDaoSupportServiceImpl<ScheduleJobGroupModel> implements ScheduleJobGroupService{

    @Override
    public List<ScheduleJobGroupModel> findByAddressType(String addressType){
        ScheduleJobGroupModel queryModel = new ScheduleJobGroupModel();
        queryModel.setAddressType(addressType);
        return this.getListByCondtion(queryModel);
    }
}
