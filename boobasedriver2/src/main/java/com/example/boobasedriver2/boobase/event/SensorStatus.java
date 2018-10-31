package com.example.boobasedriver2.boobase.event;

/**
 * create by zzh on 2018/8/10
 */
public class SensorStatus {

    /**记录各个传感器是否有异常状态，true为有异常、false正常*/
    private BoobaseSensor msg;

    public SensorStatus() {
    }

    public SensorStatus(BoobaseSensor msg) {
        this.msg = msg;
    }

    public BoobaseSensor getMsg() {
        return msg;
    }

    public void setMsg(BoobaseSensor msg) {
        this.msg = msg;
    }
}
