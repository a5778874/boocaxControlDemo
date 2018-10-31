package com.example.boobasedriver2.boobase.event;

/**
 * create by zzh on 2018/8/9
 */
public class WifiConfigureStatus {

    //配置wifi状态
    /**
     * Wifi配置成功
     */
    public static final int BOOCAX_WIFI_CONFIGURE_SUCCESS = 0;
    /**
     * 无法找到网络
     */
    public static final int BOOCAX_WIFI_NOT_FOUND = 1;
    /**
     * 密码错误
     */
    public static final int BOOCAX_WIFI_ERROR_PASSWORD = 2;
    /**
     * 连接失败
     */
    public static final int BOOCAX_WIFI_CONFIGURE_FAIL = 3;
    /**
     * ssid 长度错误，超过 32 字节
     */
    public static final int BOOCAX_WIFI_SSID_LENGTH_ILLEGAL = 4;
    /**
     * 密码长度错误 长度错误，超过 16 字节
     */
    public static final int BOOCAX_WIFI_PASSWORD_LENGTH_ILLEGAL = 5;

    private int msg;

    public WifiConfigureStatus() {
    }

    public WifiConfigureStatus(int msg) {
        this.msg = msg;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}
