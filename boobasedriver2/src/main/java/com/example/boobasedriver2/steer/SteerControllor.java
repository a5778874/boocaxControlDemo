package com.example.boobasedriver2.steer;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
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

    public static final int Action_Reset = 100;
    public static final int Action_HandupLeft = 101;
    public static final int Action_HandupRight = 102;
    public static final int Action_Goodbye = 103;
    public static final int Action_Handshake = 104;
    public static final int Action_Hug = 105;

    public static int actionStatus = -1;  //    当前正在做的动作

    private int actionCount;
    private HandlerThread handlerThread;
    private Handler handler;


    private SteerControllor() {
        initSerial();
        initHandler();
    }


    private void initHandler() {
        handlerThread = new HandlerThread("SteerHandler");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Action_Goodbye:
                        //再见动作
                        goodbyeAction();
                        break;
                    case Action_Reset:
                        //复位
                        resetAction();
                        break;
                    case Action_HandupLeft:
                        //举左手
                        handupLeftAction();
                        break;
                    case Action_HandupRight:
                        //举右手
                        handupRightAction();
                        break;
                    case Action_Handshake:
                        //握手
                        handshakeAction();
                        break;
                    case Action_Hug:
                        //拥抱
                        hugAction();
                        break;
                }

            }
        };
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

    //复位舵机
    public void resetBody() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(Action_Reset);
    }


    //伸左手
    public void handUpLeft() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(Action_HandupLeft);
    }


    //伸右手
    public void handUpRight() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(Action_HandupRight);
    }

    //再见
    public void goodbye() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(Action_Goodbye);
    }

    //握手
    public void handshake() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(Action_Handshake);
    }

    //拥抱
    public void hug() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(Action_Hug);
    }

    //------------------------------舵机连贯动作--------------------------

    //复位
    private void resetAction() {
        actionStatus = Action_Reset;
        int angle = 0;
        int speed = 10;
        sendActionGroup(new SteerBean(angle, speed, SteerBody.ShoulderLeft),
                new SteerBean(angle, speed, SteerBody.ArmLeft),
                new SteerBean(angle, speed, SteerBody.ElbowLeft),
                new SteerBean(angle, speed, SteerBody.ShoulderRight),
                new SteerBean(angle, speed, SteerBody.ArmRight),
                new SteerBean(angle, speed, SteerBody.ElbowRight));
    }

    //伸右手
    private void handupRightAction() {
        actionStatus = Action_HandupRight;
        actionCount = 0;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (actionCount) {
                    case 0:
                        sendActionGroup(new SteerBean(30, 8, SteerBody.ShoulderRight),
                                new SteerBean(45, 8, SteerBody.ArmRight),
                                new SteerBean(70, 11, SteerBody.ElbowRight));
                        handler.postDelayed(this, 3000);
                        break;
                    case 1:
                        resetBody();
                        break;
                    default:
                        break;
                }
                actionCount++;
            }
        }, 0);

    }

    //伸左手
    private void handupLeftAction() {
        actionStatus = Action_HandupLeft;
        actionCount = 0;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (actionCount) {
                    case 0:
                        sendActionGroup(new SteerBean(30, 8, SteerBody.ShoulderLeft),
                                new SteerBean(45, 8, SteerBody.ArmLeft),
                                new SteerBean(-70, 11, SteerBody.ElbowLeft));
                        handler.postDelayed(this, 3000);
                        break;
                    case 1:
                        resetBody();
                        break;
                    default:
                        break;
                }
                actionCount++;
            }
        }, 0);
    }


    //再见
    private void goodbyeAction() {
        actionStatus = Action_Goodbye;
        actionCount = 0;
        final SteerBean shoulder160 = new SteerBean(160, 10, SteerBody.ShoulderRight);
        final SteerBean arm15 = new SteerBean(15, 7, SteerBody.ArmRight);
        final SteerBean arm45 = new SteerBean(45, 7, SteerBody.ArmRight);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (actionCount) {
                    case 0:
                        sendActionGroup(shoulder160, arm15);
                        handler.postDelayed(this, 3000);
                        break;
                    case 1:
                        sendActionGroup(arm45);
                        handler.postDelayed(this, 800);
                        break;
                    case 2:
                        sendActionGroup(arm15);
                        handler.postDelayed(this, 800);
                        break;
                    case 3:
                        sendActionGroup(arm45);
                        handler.postDelayed(this, 800);
                        break;
                    case 4:
                        sendActionGroup(arm15);
                        handler.postDelayed(this, 800);
                        break;
                    case 5:
                        resetBody();
                        break;
                    default:
                        break;
                }
                actionCount++;
            }
        }, 0);
    }


    //握手
    private void handshakeAction() {
        actionStatus = Action_Hug;
        actionCount = 0;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (actionCount) {
                    case 0:
                        sendAction(45, 8, SteerBody.ShoulderRight);
                        handler.postDelayed(this, 3000);
                        break;
                    case 1:
                        resetBody();
                        break;
                    default:
                        break;
                }
                actionCount++;
            }
        }, 0);

    }

    //拥抱
    private void hugAction() {
        actionStatus = Action_Hug;
        actionCount = 0;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (actionCount) {
                    case 0:
                        sendActionGroup(new SteerBean(30, 8, SteerBody.ShoulderLeft),
                                new SteerBean(20, 8, SteerBody.ArmLeft),
                                new SteerBean(-30, 8, SteerBody.ElbowLeft),
                                new SteerBean(30, 8, SteerBody.ShoulderRight),
                                new SteerBean(20, 8, SteerBody.ArmRight),
                                new SteerBean(30, 8, SteerBody.ElbowRight));
                        handler.postDelayed(this, 3000);
                        break;
                    case 1:
                        resetBody();
                        break;
                    default:
                        break;
                }
                actionCount++;
            }
        }, 0);
    }

}
