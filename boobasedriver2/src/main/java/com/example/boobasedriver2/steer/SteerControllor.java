package com.example.boobasedriver2.steer;

import android.util.Log;

import com.example.boobasedriver2.boobase.BaseControllor;
import com.gjdl.common.thread.CachedTreadPoolOperator;
import com.iflytek.aiui.uartkit.util.SerialDataUtils;
import com.iflytek.aiui.uartkit.util.SerialPortUtil;

import java.util.ArrayList;
import java.util.List;

public class SteerControllor extends BaseControllor {
    private SerialPortUtil serialPortUtil;
    private String controlCode = "";//按协议生成的控制命令

    private static SteerControllor instance;

    public static String steerCom = SerialPortUtil.SERIAL_PORT_COM0;
    public static int steerRate = SerialPortUtil.SERIAL_BAUDRATE_9600;

    private SteerControllor() {
        initSerial();
    }


    public static synchronized SteerControllor getInstance() {
        if (instance == null) {
            instance = new SteerControllor();
        }
        return instance;
    }

    private void initSerial() {
        Log.d("TAG1", "initSerial: " + steerCom + "," + steerRate);
        serialPortUtil = SerialPortUtil.getInstance(steerCom, steerRate);
        serialPortUtil.setOnDataReceiveListener(new SerialPortUtil.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                Log.d("TAG1", "onDataReceive: " + SerialDataUtils.ByteArrToHex(buffer));
            }
        });
    }

    //发送单个部位动作，角度，速度，部位
    public void sendAction(int angle, int speed, int bodyId) {
        List<SteerBean> list = new ArrayList<>();
        list.add(new SteerBean(angle, speed, bodyId));
        controlCode = SteerCMDConvertor.getInstance().formatCmd(list);
        sendCmds();
    }


    //发送同时控制多个部位动作，角度，速度，部位
    public void sendActionGroup(SteerBean... steerBeans) {
        List<SteerBean> list = new ArrayList<>();
        for (SteerBean steerBean : steerBeans) {
            list.add(steerBean);
        }
        controlCode = SteerCMDConvertor.getInstance().formatCmd(list);
        sendCmds();
    }

    //发送同时控制多个部位动作，角度，速度，部位
    public void sendActionGroup(List<SteerBean> list) {
        controlCode = SteerCMDConvertor.getInstance().formatCmd(list);
        sendCmds();
    }

    //复位舵机
    public void resetBody() {
        int angle = 0;
        int speed = 10;
        sendActionGroup(new SteerBean(angle, speed, SteerBody.ShoulderLeft),
                new SteerBean(angle, speed, SteerBody.ArmLeft),
                new SteerBean(angle, speed, SteerBody.ElbowLeft),
                new SteerBean(angle, speed, SteerBody.ShoulderRight),
                new SteerBean(angle, speed, SteerBody.ArmRight),
                new SteerBean(angle, speed, SteerBody.ElbowRight));
    }

    //左手
    public void handUpLeft() {
        sendActionGroup(new SteerBean(30, 8, SteerBody.ShoulderLeft),
                new SteerBean(45, 8, SteerBody.ArmLeft),
                new SteerBean(-70, 11, SteerBody.ElbowLeft));
    }

    //右手
    public void handUpRight() {
        sendActionGroup(new SteerBean(30, 8, SteerBody.ShoulderRight),
                new SteerBean(45, 8, SteerBody.ArmRight),
                new SteerBean(70, 11, SteerBody.ElbowRight));
    }


    //再见手势
    public void goodbye() {
        final SteerBean shoulder160 = new SteerBean(160, 10, SteerBody.ShoulderRight);
        final SteerBean arm15 = new SteerBean(15, 9, SteerBody.ArmRight);
        final SteerBean arm45 = new SteerBean(45, 9, SteerBody.ArmRight);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendActionGroup(shoulder160, arm15);
                    Thread.sleep(3200);
                    sendActionGroup(arm45);
                    Thread.sleep(1500);
                    sendActionGroup(arm15);
                    Thread.sleep(1500);
                    sendActionGroup(arm45);
                    Thread.sleep(1500);
                    sendActionGroup(arm15);
                    Thread.sleep(1500);
                    resetBody();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 开始发送控制命令到串口
     */
    private void sendCmds() {
        CachedTreadPoolOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                serialPortUtil.sendCmds(controlCode);
                Log.d("TAG1", "steer sendCmds: " + controlCode + "..thread:" + Thread.currentThread().getName());
            }
        });
    }


}
