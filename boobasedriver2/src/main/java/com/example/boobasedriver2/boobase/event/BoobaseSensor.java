package com.example.boobasedriver2.boobase.event;

//传感器状态，true表示有异常，false正常
public class BoobaseSensor {
    /**
     * uwb状态
     */
    private boolean uwbErrorStatus;

    /**
     * 激光状态
     */
    private boolean laserErrorStatus;

    /**
     * 底盘状态
     */
    private boolean boobaseErrorStatus;

    /**
     * 码盘状态
     */
    private boolean encodingDiskErrorStatus;

    /**
     * 摄像头状态
     */
    private boolean cameraErrorStatus;


    public BoobaseSensor() {
    }

    public BoobaseSensor(boolean uwbErrorStatus, boolean laserErrorStatus, boolean boobaseErrorStatus, boolean encodingDiskErrorStatus, boolean cameraErrorStatus) {
        this.uwbErrorStatus = uwbErrorStatus;
        this.laserErrorStatus = laserErrorStatus;
        this.boobaseErrorStatus = boobaseErrorStatus;
        this.encodingDiskErrorStatus = encodingDiskErrorStatus;
        this.cameraErrorStatus = cameraErrorStatus;
    }

    public boolean isUwbErrorStatus() {
        return uwbErrorStatus;
    }

    public void setUwbErrorStatus(boolean uwbErrorStatus) {
        this.uwbErrorStatus = uwbErrorStatus;
    }

    public boolean isLaserErrorStatus() {
        return laserErrorStatus;
    }

    public void setLaserErrorStatus(boolean laserErrorStatus) {
        this.laserErrorStatus = laserErrorStatus;
    }

    public boolean isBoobaseErrorStatus() {
        return boobaseErrorStatus;
    }

    public void setBoobaseErrorStatus(boolean boobaseErrorStatus) {
        this.boobaseErrorStatus = boobaseErrorStatus;
    }

    public boolean isEncodingDiskErrorStatus() {
        return encodingDiskErrorStatus;
    }

    public void setEncodingDiskErrorStatus(boolean encodingDiskErrorStatus) {
        this.encodingDiskErrorStatus = encodingDiskErrorStatus;
    }

    public boolean isCameraErrorStatus() {
        return cameraErrorStatus;
    }

    public void setCameraErrorStatus(boolean cameraErrorStatus) {
        this.cameraErrorStatus = cameraErrorStatus;
    }


}
