package com.example.boobasedriver.eventbus;

/**
 * create by zzh on 2018/8/9
 */
public class ChargeStatus {
    /**
     * 从底盘发送过来的未处理原命令
     */
    private byte [] respondCmd;

    /**
     * 充电状态：
     * 0-未充电状态：正常运行；
     * 1-充电状态：正在连接底座充电；
     * 2-充电状态：正在连接线缆充电；
     * 3-对接过程中：正在和底座对接；
     * 4-脱离过程中：正在脱离充电座；
     * 5-充电状态：已充满，但仍连接在充电座上（需发送取消充电命令终止充电）；
     */
    private int ChargeStatus;

    public ChargeStatus(byte[] respondCmd, int chargeStatus) {
        this.respondCmd = respondCmd;
        ChargeStatus = chargeStatus;
    }

    public byte[] getRespondCmd() {
        return respondCmd;
    }

    public void setRespondCmd(byte[] respondCmd) {
        this.respondCmd = respondCmd;
    }

    public int getChargeStatus() {
        return ChargeStatus;
    }

    public void setChargeStatus(int chargeStatus) {
        ChargeStatus = chargeStatus;
    }
}
