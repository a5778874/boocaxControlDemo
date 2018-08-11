package com.example.boobasedriver.constant;

import com.zzh.serialportkit.utlis.SerialPortUtil;

/**
 * create by zzh on 2018/8/1
 * 用于存放一些常量值
 */
public class SerialPortProperties {

    public static String SERIAL_PORT_COM0 = "/dev/ttyS0";
    public static String SERIAL_PORT_COM1 = "/dev/ttyS1";
    public static String SERIAL_PORT_COM2 = "/dev/ttyS2";
    public static String SERIAL_PORT_COM3 = "/dev/ttyS3";
    public static String SERIAL_PORT_COM4 = "/dev/ttyS4";

    public static int BAUDRATE = 9600;

    //    public static String Param_VX = "Vx";
    //   public static String Param_VY = "Vy";

    /**
     * sharePreference中存放速度信息的key
     */
    public static String Key_Velocity = "Velocity";
    /**
     * sharePreference中存放角度偏移量信息的key
     */
    public static String Key_Yaw = "Yaw";

    /**
     * sharePreference中存放移动模式的key
     */
    public static String Key_MOVE_TYPE = "MoveType";


}
