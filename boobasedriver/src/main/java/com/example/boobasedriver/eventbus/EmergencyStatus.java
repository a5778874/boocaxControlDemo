package com.example.boobasedriver.eventbus;

/**
 * create by zzh on 2018/8/9
 */
public class EmergencyStatus {
    /**
     * 从底盘发送过来的未处理原命令
     */
    private byte [] respondCmd;

    /**
     * 紧急开关按钮状态：
     * 0-开机后状态一直抬起未动过；
     * 1-抬起状态；
     * 2-按下状态；
     */
    private int EmergencyStatus;

    public EmergencyStatus(byte[] respondCmd, int emergencyStatus) {
        this.respondCmd = respondCmd;
        EmergencyStatus = emergencyStatus;
    }

    public byte[] getRespondCmd() {
        return respondCmd;
    }

    public void setRespondCmd(byte[] respondCmd) {
        this.respondCmd = respondCmd;
    }

    public int getEmergencyStatus() {
        return EmergencyStatus;
    }

    public void setEmergencyStatus(int emergencyStatus) {
        EmergencyStatus = emergencyStatus;
    }
}
