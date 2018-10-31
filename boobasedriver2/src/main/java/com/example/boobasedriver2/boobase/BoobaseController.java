package com.example.boobasedriver2.boobase;


import android.app.Application;
import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;

import com.example.boobasedriver2.boobase.entity.LocationEntity;
import com.example.boobasedriver2.boobase.inter.IControllor;


/**
 * create by zzh on 2018/10/22
 */
public class BoobaseController {

    private static BoobaseController instance;
    private Context context;
    private static IControllor controllor = null;   //控制底盘操作类


    private BoobaseController(Context context) {
        this.context = context;
    }


    /**
     * 在Application注册,初始化控制器和位置信息
     *
     * @param context
     */
    public static synchronized void regist(Application context) {
        if (instance == null) {
            instance = new BoobaseController(context);
        }
        LocationsManager.CreateInstance();
        bindBoobaseService(context);
    }

    //绑定底盘控制服务
    private static void bindBoobaseService(Context context) {
        BoobaseService.bind(context, new BoobaseService.BindServiceListener() {
            @Override
            public void ServiceConnected(IControllor boobaseControllor) {
                controllor = boobaseControllor;
            }
        });
    }


    //得到控制器
    public static synchronized BoobaseController getControl() {
        return instance;
    }

    /**
     * 底盘连接wifi
     * @param ssid wifi名
     * @param psw  密码
     */
    public void connectWifi(String ssid,String psw) {
        if (controllor != null) controllor.configureWIFI(ssid,psw);
    }

    public void moveForward() {
        if (controllor != null) controllor.moveForward();
    }

    public void moveBackward() {
        if (controllor != null) controllor.moveBackward();
    }

    public void turnLeft() {
        if (controllor != null) controllor.turnLeft();
    }

    public void turnRight() {
        if (controllor != null) controllor.turnRight();
    }

    public void stopMove() {
        if (controllor != null) controllor.stopMove();
    }


    /**
     *  导航到某个坐标
     */
    public void navigationTo(float x, float y, float yaw) {
        if (controllor != null) controllor.navigationTo(x,y,yaw);
    }

    /**
     *  导航到某个地方
     */
    public void navigationTo(String name) {
        if (LocationsManager.getInstance().isLocationExits(name)){
            LocationEntity.CoordinateBean coordinateBean = LocationsManager.getInstance().getCoordinateMap().get(name);
            navigationTo(coordinateBean.getX(),coordinateBean.getY(),coordinateBean.getYaw());
        }
    }




    /**
     * 开始充电
     */
    public void chargeStart() {
        if (controllor != null) controllor.chargeStart();
    }

    /**
     * 取消充电
     */
    public void chargeCancel() {
        if (controllor != null) controllor.chargeCancel();
    }


}
