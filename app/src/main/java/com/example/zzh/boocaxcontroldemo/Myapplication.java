package com.example.zzh.boocaxcontroldemo;

import android.app.Application;

import com.example.boobasedriver2.RobotManager;
import com.example.boobasedriver2.boobase.BoobaseControllor;
import com.example.boobasedriver2.utils.PathManager;
import com.squareup.leakcanary.LeakCanary;

/**
 * create by zzh on 2018/10/22
 */
public class Myapplication extends Application {
    private static Myapplication mInstance;


    public String station = "前台"; //常驻位置点
    public String cur_city = "广州市";
    public String robotName = "小灯泡";//机器人名字


    public static Myapplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        LeakCanary.install(this);
        PathManager.initPath();
        RobotManager.createInstance(this);

    }
}
