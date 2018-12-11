package com.example.boobasedriver2;

import android.content.Context;
import android.util.Log;

import com.example.boobasedriver2.boobase.BoobaseControllor;
import com.example.boobasedriver2.boobase.LocationsManager;
import com.example.boobasedriver2.boobase.entity.LocationEntity;

/**
 * create by zzh on 2018/11/5
 */
public class RobotManager {

    private Context context;
    private static RobotManager instance;
    private BoobaseControllor boobaseControllor;
    private LocationsManager locationsManager;


    private RobotManager(Context context) {
        this.context = context;
        boobaseControllor = BoobaseControllor.createInstance();
        locationsManager = LocationsManager.CreateInstance();
    }


    public static synchronized void createInstance(Context context) {
        if (instance == null) {
            instance = new RobotManager(context);
        }
    }


    public static RobotManager getControl() {
        return instance;
    }


    /**
     * 底盘连接wifi
     *
     * @param ssid wifi名
     * @param psw  密码
     */
    public void connectWifi(String ssid, String psw) {
        boobaseControllor.configureWIFI(ssid, psw);
    }

    public void moveForward() {
        boobaseControllor.moveForward();
    }

    public void moveBackward() {
        boobaseControllor.moveBackward();
    }

    public void turnLeft() {
        boobaseControllor.turnLeft();
    }

    public void turnRight() {
        boobaseControllor.turnRight();
    }

    public void stopMove() {
        boobaseControllor.stopMove();
    }


    public void rotationTo(float raw) {
        float rot = (float) (raw * (Math.PI / 180)); //角度转弧度
        boobaseControllor.rotationTo(rot);
    }


    /**
     * 导航到某个坐标
     */
    public void navigationTo(float x, float y, float yaw) {
        boobaseControllor.navigationTo(x, y, yaw);
    }

    /**
     * 导航到某个地方
     */
    public void navigationTo(String name) {
        if (locationsManager.isLocationExits(name)) {
            LocationEntity.CoordinateBean coordinateBean = locationsManager.getCoordinateMap().get(name);
            Log.d("TAG", coordinateBean.toString());
            navigationTo(coordinateBean.getX(), coordinateBean.getY(), coordinateBean.getYaw());
        }
    }

    /**
     * 开始充电
     */
    public void chargeStart() {
        boobaseControllor.chargeStart();
    }

    /**
     * 取消充电
     */
    public void chargeCancel() {
        boobaseControllor.chargeCancel();
    }

}
