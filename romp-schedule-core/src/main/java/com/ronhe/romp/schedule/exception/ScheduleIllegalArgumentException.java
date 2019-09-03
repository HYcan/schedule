package com.ronhe.romp.schedule.exception;

public class ScheduleIllegalArgumentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ScheduleIllegalArgumentException(String message) {
        super(message);
    }

    public ScheduleIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
