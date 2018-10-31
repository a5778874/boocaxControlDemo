package com.example.boobasedriver2.boobase.event;

/**
 * create by zzh on 2018/8/9
 */
public class EmergencyStatus {

    // 紧急开关按钮状态
    /**
     * 紧急开关开机后状态一直抬起未动过；
     */
    public static final int BOOCAX_EMERGENCY_NO_CHANGE = 0;
    /**
     * 紧急开关抬起状态；
     */
    public static final int BOOCAX_EMERGENCY_UP = 1;
    /**
     * 紧急开关被按下；
     */
    public static final int BOOCAX_EMERGENCY_PRESS = 2;

    private int msg;

    public EmergencyStatus() {
    }

    public EmergencyStatus(int msg) {
        this.msg = msg;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}
