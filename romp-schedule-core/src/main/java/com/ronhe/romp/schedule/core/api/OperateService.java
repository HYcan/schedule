package com.ronhe.romp.schedule.core.api;

import com.ronhe.romp.schedule.core.base.biz.model.ReturnT;
import com.ronhe.romp.schedule.core.scheduler.model.XxlJobInfo;
import com.ronhe.romp.schedule.core.scheduler.thread.JobTriggerPoolHelper;
import com.ronhe.romp.schedule.core.scheduler.trigger.TriggerTypeEnum;
import com.ronhe.romp.schedule.core.service.XxlJobService;

import javax.annotation.Resource;

public class OperateService {

    @Resource
    private XxlJobService xxlJobService;

    /**
     * @param jobId 任务Id
     * @param executorParam 任务执行参数
     */
    public static void triggerJob(String jobId, String executorParam){
        triggerJob(jobId, executorParam, -1);
    }

    /**
     * @param jobId 任务Id
     * @param executorParam 任务执行参数
     * @param failRetryCount 任务执行失败重试次数
     */
    public static void triggerJob(String jobId, String executorParam, int failRetryCount){
        JobTriggerPoolHelper.trigger(jobId, TriggerTypeEnum.MANUAL, failRetryCount, null, executorParam);
    }

    public ReturnT<String> addJob(XxlJobInfo jobInfo1) {
        XxlJobInfo jobInfo = new XxlJobInfo();

        /*jobInfo.setJobGroup();
        jobInfo.setJobCron();
        jobInfo.setJobDesc();
        jobInfo.setAddTime();
        jobInfo.setUpdateTime();
        jobInfo.setAuthor();
        jobInfo.setAlarmEmail();
        jobInfo.setExecutorRouteStrategy();
        jobInfo.setExecutorHandler();
        jobInfo.setExecutorParam();
        jobInfo.setExecutorBlockStrategy();
        jobInfo.setExecutorTimeout();
        jobInfo.setExecutorFailRetryCount();
        jobInfo.setGlueType();
        jobInfo.setGlueSource();
        jobInfo.setGlueRemark();
        jobInfo.setGlueUpdatetime();
        jobInfo.setChildJobId();
        jobInfo.setTriggerStatus();
        jobInfo.setTriggerLastTime();
        jobInfo.setTriggerNextTime();*/

        return xxlJobService.add(jobInfo);
    }

    public ReturnT<String> updateJob(XxlJobInfo jobInfo) {
        return xxlJobService.update(jobInfo);
    }

    public ReturnT<String> removeJob(int id) {
        return xxlJobService.remove(id);
    }

    public ReturnT<String> pauseJob(int id) {
        return xxlJobService.stop(id);
    }

    public ReturnT<String> start(int id) {
        return xxlJobService.start(id);
    }
}
