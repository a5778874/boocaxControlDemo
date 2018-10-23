package com.example.boobasedriver2.boobase;

/**
 * create by zzh on 2018/8/1
 * 底盘控制相关的功能码
 */
public enum BoobaseCMD {
    //机器人往 app 发送的命令
    MOVE_STATUS("01"),        //导航状态通知
    LOCATION_STATUS("02"),   //定位状态通知
    CHARGE_STATUS("03"),     //充电状态通知
    EMERGENCY_STATUS("04"),  //紧急按钮状态通知
    AGV_STATUS("05"),        //AGV 虚拟巡线状态通知
    SENSOR_STATUS("06"),     //底盘以及传感器状态通知


    //app往机器人发送的命令
    SAFE_MOVE("0A"),          //安全移动,遇到障碍物不再移动
    FORCEDLY_MOVE("0B"),     //强制移动
    NAVIGATION("0C"),        //导航命令
    MOVE_STOP("0E"),        //停止移动
    ROTATION("0F"),          //原地旋转
    POI_BY_INDEX("10"),     //通过索引到POI点
    POI_BY_NAME("11"),      //指定名字到POI点
    TRAVEL_RANDOM("12"),   //随机漫游
    TRAVEL_ORDINAL("13"),  //顺序漫游
    TRAVEL_CONTINUE("14"), //继续漫游
    CHARGE_START("15"),    //开始充电
    CHARGE_CANCEL("16"),   //取消充电
    CONFIGURE_WIFI("50");  //配置wifi

    private String functionCode;

    BoobaseCMD(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getFunctionCode() {
        return functionCode;
    }
}