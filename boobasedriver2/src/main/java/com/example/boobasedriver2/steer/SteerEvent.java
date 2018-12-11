package com.example.boobasedriver2.steer;

/**
 * create by zzh on 2018/11/27
 */
public class SteerEvent {
    private boolean isSuccess;

    public SteerEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public SteerEvent() {
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
