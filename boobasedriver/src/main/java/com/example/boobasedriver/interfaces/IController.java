package com.example.boobasedriver.interfaces;

import android.os.RemoteException;

/**
 * create by zzh on 2018/8/2
 */
public interface IController {
    void moveForward();  //向前移动

    void moveBackward(); //向后移动

    void turnLeft();     //向左转90度

    void turnRight();    //向右转90度

    void stopMove();     //停止移动

    void navigationTo(float x,float y,float raw); //导航到指定坐标点

    void rotationTo(float rot); //原地旋转

    void poiTo(int index);      //指定poi点索引导航到poi点

    void poiTo(String name);    //通过poi点名称导航到poi点

    void travelRandom(float sleepTime);    //随机漫游

    void travelOrdinal(float sleepTime,boolean loop);   //顺序漫游

    void travelContinue();  //继续漫游

    void travelStop();   //停止漫游

    void chargeStart();     //开始充电

    void chargeCancel();    //取消充电

    void configureWIFI(String ssid,String password);   //配置wifi


}
