package com.ronhe.romp.schedule.core.service.impl;

import com.ronhe.romp.schedule.console.model.ScheduleJobLogModel;
import com.ronhe.romp.schedule.console.model.ScheduleJobModel;
import com.ronhe.romp.schedule.console.model.ScheduleJobRegistryModel;
import com.ronhe.romp.schedule.console.service.ScheduleJobLogService;
import com.ronhe.romp.schedule.console.service.ScheduleJobRegistryService;
import com.ronhe.romp.schedule.console.service.ScheduleJobService;
import com.ronhe.romp.schedule.core.base.biz.AdminBiz;
import com.ronhe.romp.schedule.core.base.biz.model.HandleCallbackParam;
import com.ronhe.romp.schedule.core.base.biz.model.RegistryParam;
import com.ronhe.romp.schedule.core.base.biz.model.ReturnT;
import com.ronhe.romp.schedule.core.base.handler.IJobHandler;
import com.ronhe.romp.schedule.core.base.util.DateUtil;
import com.ronhe.romp.schedule.core.dao.XxlJobInfoDao;
import com.ronhe.romp.schedule.core.dao.XxlJobLogDao;
import com.ronhe.romp.schedule.core.dao.XxlJobRegistryDao;
import com.ronhe.romp.schedule.core.scheduler.model.XxlJobInfo;
import com.ronhe.romp.schedule.core.scheduler.thread.JobTriggerPoolHelper;
import com.ronhe.romp.schedule.core.scheduler.trigger.TriggerTypeEnum;
import com.ronhe.romp.schedule.core.scheduler.util.I18nUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * @author xuxueli 2017-07-27 21:54:20
 */
@Service
public class AdminBizImpl implements AdminBiz {
    private static Logger logger = LoggerFactory.getLogger(AdminBizImpl.class);

    //@Resource
    //public XxlJobLogDao xxlJobLogDao;
    @Resource
    public ScheduleJobLogService scheduleJobLogService;
    //@Resource
    @Resource
    private ScheduleJobService scheduleJobService;
    //@Resource
    //private XxlJobRegistryDao xxlJobRegistryDao;
    @Resource
    public ScheduleJobRegistryService scheduleJobRegistryService;

    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        for (HandleCallbackParam handleCallbackParam: callbackParamList) {
            ReturnT<String> callbackResult = callback(handleCallbackParam);
            logger.debug(">>>>>>>>> JobApiController.callback {}, handleCallbackParam={}, callbackResult={}",
                    (callbackResult.getCode()== IJobHandler.SUCCESS.getCode()?"success":"fail"), handleCallbackParam, callbackResult);
        }

        return ReturnT.SUCCESS;
    }

    private ReturnT<String> callback(HandleCallbackParam handleCallbackParam) {
        // valid log item
        ScheduleJobLogModel log = scheduleJobLogService.getById(handleCallbackParam.getLogId());
        if (log == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "log item not found.");
        }
        if (log.getHandleCode() > 0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "log repeate callback.");     // avoid repeat callback, trigger child job etc
        }

        // trigger success, to trigger child job
        String callbackMsg = null;
        if (IJobHandler.SUCCESS.getCode() == handleCallbackParam.getExecuteResult().getCode()) {
            ScheduleJobModel xxlJobInfo = scheduleJobService.getById(log.getJobId());
            if (xxlJobInfo!=null && xxlJobInfo.getChildJobId()!=null && xxlJobInfo.getChildJobId().trim().length()>0) {
                callbackMsg = "<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>"+ I18nUtil.getString("jobconf_trigger_child_run") +"<<<<<<<<<<< </span><br>";

                String[] childJobIds = xxlJobInfo.getChildJobId().split(",");
                for (int i = 0; i < childJobIds.length; i++) {
                    //int childJobId = (childJobIds[i]!=null && childJobIds[i].trim().length()>0 && isNumeric(childJobIds[i]))?Integer.valueOf(childJobIds[i]):-1;
                    //if (childJobId > 0) {
                    if (StringUtils.isNotBlank(childJobIds[i])) {

                        JobTriggerPoolHelper.trigger(childJobIds[i], TriggerTypeEnum.PARENT, -1, null, null);
                        ReturnT<String> triggerChildResult = ReturnT.SUCCESS;

                        // add msg
                        callbackMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg1"),
                                (i+1),
                                childJobIds.length,
                                childJobIds[i],
                                (triggerChildResult.getCode()==ReturnT.SUCCESS_CODE?I18nUtil.getString("system_success"):I18nUtil.getString("system_fail")),
                                triggerChildResult.getMsg());
                    } else {
                        callbackMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg2"),
                                (i+1),
                                childJobIds.length,
                                childJobIds[i]);
                    }
                }

            }
        }

        // handle msg
        StringBuffer handleMsg = new StringBuffer();
        if (log.getHandleMsg()!=null) {
            handleMsg.append(log.getHandleMsg()).append("<br>");
        }
        if (handleCallbackParam.getExecuteResult().getMsg() != null) {
            handleMsg.append(handleCallbackParam.getExecuteResult().getMsg());
        }
        if (callbackMsg != null) {
            handleMsg.append(callbackMsg);
        }

        // success, save log
        log.setHandleTime(DateUtil.formatDateTime(new Date()));
        log.setHandleCode(handleCallbackParam.getExecuteResult().getCode());
        log.setHandleMsg(handleMsg.toString());
        scheduleJobLogService.update(log);

        return ReturnT.SUCCESS;
    }

    private boolean isNumeric(String str){
        try {
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        ScheduleJobRegistryModel jobRegistry = new ScheduleJobRegistryModel();
        jobRegistry.setRegistryGroup(registryParam.getRegistGroup());
        jobRegistry.setRegistryKey(registryParam.getRegistryKey());
        jobRegistry.setRegistryValue(registryParam.getRegistryValue());
        List<ScheduleJobRegistryModel> jobRegistryList = scheduleJobRegistryService.getListByCondtion(jobRegistry);
        if(jobRegistryList != null && jobRegistryList.size() > 0){
            for(ScheduleJobRegistryModel jr : jobRegistryList){
                jr.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                scheduleJobRegistryService.saveOrUpdate(jr);
            }
        } else{
            jobRegistry.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            scheduleJobRegistryService.save(jobRegistry);
        }

        /*int ret = xxlJobRegistryDao.registryUpdate(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        if (ret < 1) {
            xxlJobRegistryDao.registrySave(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        }*/
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        scheduleJobRegistryService.registryDelete(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        return ReturnT.SUCCESS;
    }

}
