package com.ronhe.romp.schedule.console.service.impl;

import org.springframework.stereotype.Service;

import com.ronhe.romp.schedule.console.model.ScheduleJobChildModel;
import com.ronhe.romp.schedule.console.service.ScheduleJobChildService;
import com.ronhe.romp.schedule.console.service.jdbc.SQLDaoSupportServiceImpl;

@Service
public class ScheduleJobChildServiceImpl extends SQLDaoSupportServiceImpl<ScheduleJobChildModel> implements ScheduleJobChildService{

}
