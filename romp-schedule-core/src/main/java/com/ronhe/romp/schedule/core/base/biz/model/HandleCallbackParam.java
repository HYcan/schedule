package com.ronhe.romp.schedule.core.base.biz.model;

import java.io.Serializable;

/**
 * Created by xuxueli on 17/3/2.
 */
public class HandleCallbackParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private String logId;
    private long logDateTim;

    private ReturnT<String> executeResult;

    public HandleCallbackParam(){}
    public HandleCallbackParam(String logId, long logDateTim, ReturnT<String> executeResult) {
        this.logId = logId;
        this.logDateTim = logDateTim;
        this.executeResult = executeResult;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public long getLogDateTim() {
        return logDateTim;
    }

    public void setLogDateTim(long logDateTim) {
        this.logDateTim = logDateTim;
    }

    public ReturnT<String> getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(ReturnT<String> executeResult) {
        this.executeResult = executeResult;
    }

    @Override
    public String toString() {
        return "HandleCallbackParam{" +
                "logId=" + logId +
                ", logDateTim=" + logDateTim +
                ", executeResult=" + executeResult +
                '}';
    }

}
