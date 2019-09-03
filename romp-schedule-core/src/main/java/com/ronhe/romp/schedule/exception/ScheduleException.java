package com.ronhe.romp.schedule.exception;

public class ScheduleException extends RuntimeException{
    public ScheduleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScheduleException(String message) {
        super(message);
    }
}
