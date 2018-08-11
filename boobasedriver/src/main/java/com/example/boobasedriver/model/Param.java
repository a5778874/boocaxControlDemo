package com.example.boobasedriver.model;

/**
 * create by zzh on 2018/8/1
 * 速度参数
 */
public class Param {
    float x;
    float y;
    float yaw;

    public Param() {

    }

    public Param(float x, float y, float yaw) {
        this.x = x;
        this.y = y;
        this.yaw = yaw;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }


    //对比3个参数的值来比较两个对象是否相等
    @Override
    public boolean equals(Object obj) {
        return Math.abs(this.x - ((Param) obj).x) <= 0.0001 && Math.abs(this.y - ((Param) obj).y) <= 0.0001 && Math.abs(this.yaw - ((Param) obj).yaw) <= 0.0001;

    }
}
