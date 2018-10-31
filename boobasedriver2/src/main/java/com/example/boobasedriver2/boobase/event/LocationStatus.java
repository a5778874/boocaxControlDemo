package com.example.boobasedriver2.boobase.event;

/**
 * create by zzh on 2018/8/9
 */
public class LocationStatus {

    //定位状态
    /**
     * 定位正常
     */
    public static final int BOOCAX_LOCATION_CORRECT = 0;
    /**
     * 定位准备，正在尝试定位；
     */
    public static final int BOOCAX_LOCATION_READY = 1;
    /**
     * (异常状态）还没构建地图，无法定位；
     */
    public static final int BOOCAX_LOCATION_NO_MAP = 2;
    /**
     * (构图状态）正在构建地图中；
     */
    public static final int BOOCAX_LOCATION_BUILDING_MAP = 3;
    /**
     * (异常状态）UWB错误
     */
    public static final int BOOCAX_LOCATION_UWB_ERROR = 4;
    /**
     * （构图状态）正在进行回环检测，优化地图；
     */
    public static final int BOOCAX_LOCATION_CHECKING_LOOPBACK = 5;

    private int msg;

    public LocationStatus() {
    }

    public LocationStatus(int msg) {
        this.msg = msg;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}
