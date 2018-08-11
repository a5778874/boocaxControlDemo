package com.example.boobasedriver.boobase;

import android.content.Context;

import com.example.boobasedriver.constant.SerialPortProperties;
import com.example.boobasedriver.enums.BoobaseCMD;
import com.example.boobasedriver.utils.SharePreferenceUtils;

/**
 * create by zzh on 2018/8/3
 */
public class BooBaseSetting {

    private Context context;
    private static BooBaseSetting Instance;

    private BooBaseSetting(Context context) {
        this.context = context;
    }


    /**
     * 获得控制参数设置实例，能配置线速度和角度信息
     */
    public static synchronized BooBaseSetting init(Context context) {
        if (Instance == null) {
            Instance = new BooBaseSetting(context.getApplicationContext());
        }
        return Instance;
    }


    /**
     * 获取配置文件中的速度信息
     */
    public float getVelocityProperties() {
        float velocity = (float) SharePreferenceUtils.getParam(context, SerialPortProperties.Key_Velocity, 0.37f);
        return velocity;
    }

    /**
     * 设置配置文件中的速度信息
     */
    public void setVelocityProperties(float velocity) {
        SharePreferenceUtils.setParam(context, SerialPortProperties.Key_Velocity, velocity);
        BooBaseController.getInstance(context).initBooBaseProperties();//应用配置
    }

    /**
     * 获取配置文件中的偏移量
     */
    public float getYawProperties() {
        float yaw = (float) SharePreferenceUtils.getParam(context, SerialPortProperties.Key_Yaw, 0f);
        return yaw;
    }

    /**
     * 设置配置文件中的偏移量
     */
    public void setYawProperties(float yaw) {
        SharePreferenceUtils.setParam(context, SerialPortProperties.Key_Yaw, yaw);
        BooBaseController.getInstance(context).initBooBaseProperties();//应用配置
    }


    /**
     * 获取配置文件中的移动模式对应的功能码
     */
    public String getMoveType() {
        String moveType = (String) SharePreferenceUtils.getParam(context, SerialPortProperties.Key_MOVE_TYPE, BoobaseCMD.SAFE_MOVE.getFunctionCode());
        return moveType;
    }

    /**
     * 移动模式的功能码写入配置文件中
     */
    public void setMoveType(MoveType moveType) {

        String functionCode;

        if (moveType.name().equals(BoobaseCMD.FORCEDLY_MOVE.name())) {
            functionCode = BoobaseCMD.FORCEDLY_MOVE.getFunctionCode();
        } else {
            functionCode = BoobaseCMD.SAFE_MOVE.getFunctionCode();
        }

        SharePreferenceUtils.setParam(context, SerialPortProperties.Key_MOVE_TYPE, functionCode);
        BooBaseController.getInstance(context).initBooBaseProperties();//应用配置
    }


    /**
     * 移动模式：安全移动，强制移动
     */
    public enum MoveType {
        SAFE_MOVE, FORCEDLY_MOVE
    }



}

