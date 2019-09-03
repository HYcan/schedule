package com.ronhe.romp.schedule.console.model;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "schedule_job_child")
public class ScheduleJobChildModel {
	    @Id
	    private String id;
	    private String childId;
	    private String jobId;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getChildId() {
			return childId;
		}
		public void setChildId(String childId) {
			this.childId = childId;
		}
		public String getJobId() {
			return jobId;
		}
		public void setJobId(String jobId) {
			this.jobId = jobId;
		}
	    
}
