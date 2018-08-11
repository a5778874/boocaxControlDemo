package com.example.boobasedriver.eventbus;

/**
 * create by zzh on 2018/8/9
 */
public class LocationStatus {
    /**
     * 从底盘发送过来的未处理原命令
     */
    private byte [] respondCmd;

    /**
     * 定位状态：
     * 0-定位正常；
     * 1-定位准备/正在尝试定位；
     * 2-(异常状态）还没构建地图，无法定位；
     * 3-(构图状态）正在构建地图中；
     * 4-(异常状态）UWB错误；
     * 5-（构图状态）正在进行回环检测，优化地图；
     */
    private int locationStatus;

    public LocationStatus(byte[] respondCmd, int locationStatus) {
        this.respondCmd = respondCmd;
        this.locationStatus = locationStatus;
    }

    public byte[] getRespondCmd() {
        return respondCmd;
    }

    public void setRespondCmd(byte[] respondCmd) {
        this.respondCmd = respondCmd;
    }

    public int getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(int locationStatus) {
        this.locationStatus = locationStatus;
    }
}
