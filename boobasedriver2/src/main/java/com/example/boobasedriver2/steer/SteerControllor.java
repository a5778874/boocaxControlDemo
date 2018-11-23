package com.example.boobasedriver2.steer;

import android.util.Log;

import com.example.boobasedriver2.boobase.BaseControllor;
import com.gjdl.common.thread.CachedTreadPoolOperator;
import com.iflytek.aiui.uartkit.util.SerialDataUtils;
import com.iflytek.aiui.uartkit.util.SerialPortUtil;

public class SteerControllor extends BaseControllor {
    private SerialPortUtil serialPortUtil;
    private String controlCode = "";//按协议生成的控制命令

    private static SteerControllor instance;

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
        serialPortUtil = SerialPortUtil.getInstance(SerialPortUtil.SERIAL_PORT_COM0, SerialPortUtil.SERIAL_BAUDRATE_9600);
        serialPortUtil.setOnDataReceiveListener(new SerialPortUtil.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                Log.d("TAG1", "onDataReceive: " + SerialDataUtils.ByteArrToHex(buffer));
            }
        });
    }

    //发送单个部位动作，角度，速度，部位
    public void sendAction(int angle, int speed, int bodyId) {
        controlCode = SteerCMDConvertor.getInstance().formatCmd(new SteerBean(angle, speed, bodyId));
        sendCmds();
    }

    //发送同时控制多个部位动作，角度，速度，部位
    public void sendActionGroup(SteerBean... steerBeans) {
        controlCode = SteerCMDConvertor.getInstance().formatCmd(steerBeans);
        sendCmds();
    }

    /**
     * 开始发送控制命令到串口
     */
    private void sendCmds() {
        CachedTreadPoolOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                serialPortUtil.sendCmds(controlCode);
                Log.d("TAG", "steer sendCmds: " + controlCode + "..thread:" + Thread.currentThread().getName());
            }
        });
    }




}
