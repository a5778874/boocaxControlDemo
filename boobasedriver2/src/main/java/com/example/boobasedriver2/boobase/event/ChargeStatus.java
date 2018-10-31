package com.example.boobasedriver2.boobase.event;

/**
 * create by zzh on 2018/8/9
 */
public class ChargeStatus {

    /**未充电状态：正常运行；*/
    public static final int BOOCAX_CHARGE_UNCHARGED= 0;
    /**充电状态：正在连接底座充电；*/
    public static final int BOOCAX_CHARGE_CHARGEING_TO_BOOBASE= 1;
    /**充电状态：正在连接线缆充电；*/
    public static final int BOOCAX_CHARGE_CHARGEING_TO_CABLE = 2;
    /**对接过程中：正在和底座对接；*/
    public static final int BOOCAX_CHARGE_MOVING_TO_BOOBASE = 3;
    /**脱离过程中：正在脱离充电座；*/
    public static final int BOOCAX_CHARGE_LEAVING_BOOBASE = 4;
    /**充电状态：已充满，但仍连接在充电座上（需发送取消充电命令终止充电）；*/
    public static final int BOOCAX_CHARGE_FULL = 5;

    private int msg;

    public ChargeStatus() {
    }

    public ChargeStatus(int msg) {
        this.msg = msg;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}
