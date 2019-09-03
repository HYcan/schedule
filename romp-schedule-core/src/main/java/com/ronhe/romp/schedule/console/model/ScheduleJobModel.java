package com.ronhe.romp.schedule.console.model;

import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.ronhe.romp.schedule.console.util.annotation.CreateTime;
import com.ronhe.romp.schedule.console.util.annotation.UpdateTime;
import com.ronhe.romp.schedule.console.util.validation.annotation.Length;
import com.ronhe.romp.schedule.console.util.validation.annotation.NotBlank;

import java.util.Date;

@Table(name = "schedule_job")
public class ScheduleJobModel {
	    @Id
	    private String id;
	    private String jobType;
	    private String jobGroup;
	    @NotBlank(message="时间表达式不能为空")
	    private String jobCron;
	    @NotBlank(message="任务描述不能为空")
	    @Length(fieldName="描述",max=30,min=0)
	    private String jobDesc;
	    private String jobAuthor;
	    
	    private String executorRouteStrategy;
	    private String executorHandler;
	    private String executorBlockStrategy;
	    private int executorFailRetryCount;
	    private int executorTimeout;
	    private String glueSource;
	    
	    private int triggerStatus;
	    private long triggerLastTime;
	    private long triggerNextTime;
	    @CreateTime
	    @OrderBy("DESC")
	    private String createTime;
	    @UpdateTime
	    private String updateTime;


		private String author;		// 负责人
		private String alarmEmail;	// 报警邮件
		private String executorParam;		    // 执行器，任务参数
		private String glueType;		// GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
		private String glueRemark;		// GLUE备注
		private Date glueUpdatetime;	// GLUE更新时间
		private String childJobId;		// 子任务ID，多个逗号分隔

	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAlarmEmail() {
		return alarmEmail;
	}
	public void setAlarmEmail(String alarmEmail) {
		this.alarmEmail = alarmEmail;
	}
	public String getExecutorParam() {
		return executorParam;
	}
	public void setExecutorParam(String executorParam) {
		this.executorParam = executorParam;
	}
	public String getGlueType() {
		return glueType;
	}
	public void setGlueType(String glueType) {
		this.glueType = glueType;
	}
	public String getGlueRemark() {
		return glueRemark;
	}
	public void setGlueRemark(String glueRemark) {
		this.glueRemark = glueRemark;
	}
	public Date getGlueUpdatetime() {
		return glueUpdatetime;
	}
	public void setGlueUpdatetime(Date glueUpdatetime) {
		this.glueUpdatetime = glueUpdatetime;
	}
	public String getChildJobId() {
		return childJobId;
	}
	public void setChildJobId(String childJobId) {
		this.childJobId = childJobId;
	}

	public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getJobType() {
			return jobType;
		}
		public void setJobType(String jobType) {
			this.jobType = jobType;
		}
		public String getJobGroup() {
			return jobGroup;
		}
		public void setJobGroup(String jobGroup) {
			this.jobGroup = jobGroup;
		}
		public String getJobCron() {
			return jobCron;
		}
		public void setJobCron(String jobCron) {
			this.jobCron = jobCron;
		}
		public String getJobDesc() {
			return jobDesc;
		}
		public void setJobDesc(String jobDesc) {
			this.jobDesc = jobDesc;
		}
		public String getJobAuthor() {
			return jobAuthor;
		}
		public void setJobAuthor(String jobAuthor) {
			this.jobAuthor = jobAuthor;
		}
		public String getExecutorRouteStrategy() {
			return executorRouteStrategy;
		}
		public void setExecutorRouteStrategy(String executorRouteStrategy) {
			this.executorRouteStrategy = executorRouteStrategy;
		}
		public String getExecutorHandler() {
			return executorHandler;
		}
		public void setExecutorHandler(String executorHandler) {
			this.executorHandler = executorHandler;
		}
		public String getExecutorBlockStrategy() {
			return executorBlockStrategy;
		}
		public void setExecutorBlockStrategy(String executorBlockStrategy) {
			this.executorBlockStrategy = executorBlockStrategy;
		}
		public int getExecutorFailRetryCount() {
			return executorFailRetryCount;
		}
		public void setExecutorFailRetryCount(int executorFailRetryCount) {
			this.executorFailRetryCount = executorFailRetryCount;
		}
		public int getExecutorTimeout() {
			return executorTimeout;
		}
		public void setExecutorTimeout(int executorTimeout) {
			this.executorTimeout = executorTimeout;
		}
		public String getGlueSource() {
			return glueSource;
		}
		public void setGlueSource(String glueSource) {
			this.glueSource = glueSource;
		}
		public int getTriggerStatus() {
			return triggerStatus;
		}
		public void setTriggerStatus(int triggerStatus) {
			this.triggerStatus = triggerStatus;
		}
		public long getTriggerLastTime() {
			return triggerLastTime;
		}
		public void setTriggerLastTime(long triggerLastTime) {
			this.triggerLastTime = triggerLastTime;
		}
		public long getTriggerNextTime() {
			return triggerNextTime;
		}
		public void setTriggerNextTime(long triggerNextTime) {
			this.triggerNextTime = triggerNextTime;
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		public String getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(String updateTime) {
			this.updateTime = updateTime;
		}
		
	    
	    
}
