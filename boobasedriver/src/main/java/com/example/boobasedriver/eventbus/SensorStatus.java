package com.example.boobasedriver.eventbus;

/**
 * create by zzh on 2018/8/10
 */
public class SensorStatus {
    /**
     * 从底盘发送过来的未处理原命令： 正常时收到AA 55 06 06 01 00 00 00 00 01
     */
    private byte[] respondCmd;


    /**
     * uwb状态，0表示正常，其他异常
     */
    private int uwbStatus;

    /**
     * 激光状态，0表示正常，其他异常
     */
    private int laserStatus;

    /**
     * 底盘状态，0表示正常，其他异常
     */
    private int boobaseStatus;

    /**
     * 码盘状态，0表示正常，其他异常
     */
    private int encodingDiskStatus;

    /**
     * 摄像头状态，0表示正常，其他异常
     */
    private int cameraStatus;

    public SensorStatus(byte[] respondCmd, int uwbStatus, int laserStatus, int boobaseStatus, int encodingDiskStatus, int cameraStatus) {
        this.respondCmd = respondCmd;
        this.uwbStatus = uwbStatus;
        this.laserStatus = laserStatus;
        this.boobaseStatus = boobaseStatus;
        this.encodingDiskStatus = encodingDiskStatus;
        this.cameraStatus = cameraStatus;
    }

    public byte[] getRespondCmd() {
        return respondCmd;
    }

    public void setRespondCmd(byte[] respondCmd) {
        this.respondCmd = respondCmd;
    }

    public int getUwbStatus() {
        return uwbStatus;
    }

    public void setUwbStatus(int uwbStatus) {
        this.uwbStatus = uwbStatus;
    }

    public int getLaserStatus() {
        return laserStatus;
    }

    public void setLaserStatus(int laserStatus) {
        this.laserStatus = laserStatus;
    }

    public int getBoobaseStatus() {
        return boobaseStatus;
    }

    public void setBoobaseStatus(int boobaseStatus) {
        this.boobaseStatus = boobaseStatus;
    }

    public int getEncodingDiskStatus() {
        return encodingDiskStatus;
    }

    public void setEncodingDiskStatus(int encodingDiskStatus) {
        this.encodingDiskStatus = encodingDiskStatus;
    }

    public int getCameraStatus() {
        return cameraStatus;
    }

    public void setCameraStatus(int cameraStatus) {
        this.cameraStatus = cameraStatus;
    }
}
