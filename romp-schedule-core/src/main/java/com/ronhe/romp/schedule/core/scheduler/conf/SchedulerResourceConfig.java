package com.ronhe.romp.schedule.core.scheduler.conf;


import com.ronhe.romp.schedule.console.service.ScheduleJobGroupService;
import com.ronhe.romp.schedule.console.service.ScheduleJobLogService;
import com.ronhe.romp.schedule.console.service.ScheduleJobRegistryService;
import com.ronhe.romp.schedule.console.service.ScheduleJobService;
import com.ronhe.romp.schedule.core.base.biz.AdminBiz;
import com.ronhe.romp.schedule.core.dao.XxlJobGroupDao;
import com.ronhe.romp.schedule.core.dao.XxlJobInfoDao;
import com.ronhe.romp.schedule.core.dao.XxlJobLogDao;
import com.ronhe.romp.schedule.core.dao.XxlJobRegistryDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Component
public class SchedulerResourceConfig implements InitializingBean {
    private static SchedulerResourceConfig adminConfig = null;

    public static SchedulerResourceConfig getAdminConfig() {
        return adminConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        adminConfig = this;
    }

    // conf
    //@Value("${xxl.job.i18n}")
    private String i18n = "";

    //@Value("${xxl.job.accessToken}")
    private String accessToken;

    //@Value("${spring.mail.username}")
    private String emailUserName;

    // dao, service

    //@Resource
    private XxlJobLogDao xxlJobLogDao;
    //@Resource
    private XxlJobInfoDao xxlJobInfoDao;
    //@Resource
    private XxlJobRegistryDao xxlJobRegistryDao;
    //@Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private ScheduleJobService scheduleJobService;

    @Resource
    private ScheduleJobGroupService scheduleJobGroupService;

    @Resource
    private ScheduleJobLogService scheduleJobLogService;

    @Resource
    private ScheduleJobRegistryService scheduleJobRegistryService;

    @Resource
    private AdminBiz adminBiz;
    //@Resource
    private JavaMailSender mailSender;
    @Resource
    private DataSource dataSource;


    public String getI18n() {
        return i18n;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmailUserName() {
        return emailUserName;
    }

    public XxlJobLogDao getXxlJobLogDao() {
        return xxlJobLogDao;
    }

    public XxlJobInfoDao getXxlJobInfoDao() {
        return xxlJobInfoDao;
    }

    public XxlJobRegistryDao getXxlJobRegistryDao() {
        return xxlJobRegistryDao;
    }

    public ScheduleJobRegistryService getScheduleJobRegistryService() {
        return scheduleJobRegistryService;
    }

    public XxlJobGroupDao getXxlJobGroupDao() {
        return xxlJobGroupDao;
    }
    public ScheduleJobGroupService getScheduleJobGroupService() {
        return scheduleJobGroupService;
    }

    public AdminBiz getAdminBiz() {
        return adminBiz;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public ScheduleJobLogService getScheduleJobLogService() {
        return scheduleJobLogService;
    }

    public ScheduleJobService getScheduleJobService() {
        return scheduleJobService;
    }
}
