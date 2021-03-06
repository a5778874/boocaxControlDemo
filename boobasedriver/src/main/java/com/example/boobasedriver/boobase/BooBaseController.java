package com.example.boobasedriver.boobase;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.util.Log;

import com.example.boobasedriver.constant.SerialPortProperties;
import com.example.boobasedriver.enums.BoobaseCMD;
import com.example.boobasedriver.eventbus.ChargeStatus;
import com.example.boobasedriver.eventbus.EmergencyStatus;
import com.example.boobasedriver.eventbus.LocationStatus;
import com.example.boobasedriver.eventbus.MoveStatus;
import com.example.boobasedriver.eventbus.SensorStatus;
import com.example.boobasedriver.interfaces.IController;
import com.example.boobasedriver.model.MyInteger;
import com.iflytek.aiui.uartkit.util.SerialDataUtils;
import com.iflytek.aiui.uartkit.util.SerialPortUtil;


import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;


/**
 * create by zzh on 2018/7/28
 * <p>
 * 底盘相关的控制类
 */
public class BooBaseController implements LifecycleObserver, IController {

    private static final String TAG = "TAG";
    private static SerialPortUtil serialPortUtil;
    private static BooBaseController instance;
    private String controlCode = "";//按协议生成的控制命令
    private BooBaseSetting setting;
    private static float velocity;  // 线速度m/s
    private static float yaw;       // 角度偏移量
    private static String MoveType;  //移动类型：强制移动，安全移动
    private Context context;

    private int controlCodeFlag = -1;  //控制标记，记录上一次操作的控制命令,每个命令的控制标记都是不重复的，避免重复调用而生成相同的控制码
    private final int FLAG_STOP_MOVE = 0;
    private final int FLAG_MOVE_FORWARD = 1;
    private final int FLAG_MOVE_BACKWARD = 2;
    private final int FLAG_TURN_LEFT = 3;
    private final int FLAG_TURN_RIGHT = 4;
    private final int FLAG_NAVIGATION_TO = 5;
    private final int FLAG_ROTATION_TO = 6;
    private final int FLAG_POI_BY_INDEX = 7;
    private final int FLAG_POI_BY_NAME = 8;
    private final int FLAG_TRAVEL_RANDOM = 9;
    private final int FLAG_TRAVEL_ORDINAL = 10;
    private final int FLAG_TRAVEL_CONTINUE = 11;
    private final int FLAG_TRAVEL_STOP = 12;
    private final int FLAG_CHARGE_START = 13;
    private final int FLAG_CHARGE_CANCEL = 14;
    private final int FLAG_CONFIG_WIFI = 15;


    private BooBaseController(Context context) {
        this.context = context;
        serialPortUtil = SerialPortUtil.getInstance(SerialPortProperties.SERIAL_PORT_COM1, SerialPortProperties.BAUDRATE);
        initBooBaseProperties();
    }

    public static synchronized BooBaseController getInstance(Context context) {
        //初始化串口

        if (instance == null) {
            instance = new BooBaseController(context.getApplicationContext());
        }
        return instance;
    }


    //初始化Sharepreference里的参数配置文件
    public void initBooBaseProperties() {
        setting = BooBaseSetting.init(context.getApplicationContext());
        velocity = setting.getVelocityProperties();
        yaw = setting.getYawProperties();
        MoveType = setting.getMoveType();

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {

    }

    private byte head1 = SerialDataUtils.HexToByte("AA");
    private byte head2 = SerialDataUtils.HexToByte("55");

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onStart() {
        if (serialPortUtil == null) {
            serialPortUtil = SerialPortUtil.getInstance(SerialPortProperties.SERIAL_PORT_COM1, SerialPortProperties.BAUDRATE);
        }

        serialPortUtil.setOnDataReceiveListener(new SerialPortUtil.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                Log.d(TAG, "boobase串口接收: " + SerialDataUtils.ByteArrToHex(buffer));

                postStatus(buffer);
            }
        });
    }


    private void postStatus(byte[] buffer) {
        if (buffer[0] == head1 && buffer[1] == head2) {
            String functionCodeHex = SerialDataUtils.Byte2Hex(buffer[3]);
            Log.d("TAG", "functionCodeHex: "+functionCodeHex+".."+BoobaseCMD.EMERGENCY_STATUS.getFunctionCode());
            if (functionCodeHex.equals(BoobaseCMD.MOVE_STATUS.getFunctionCode())) {
                //移动状态通知
                EventBus.getDefault().post(new MoveStatus(buffer, (int) buffer[4]));
            } else if (functionCodeHex.equals(BoobaseCMD.LOCATION_STATUS.getFunctionCode())) {
                //位置状态通知
                EventBus.getDefault().post(new LocationStatus(buffer, (int) buffer[4]));
            } else if (functionCodeHex.equals(BoobaseCMD.CHARGE_STATUS.getFunctionCode())) {
                //充电状态通知
                EventBus.getDefault().post(new ChargeStatus(buffer, (int) buffer[4]));
            } else if (functionCodeHex.equals(BoobaseCMD.EMERGENCY_STATUS.getFunctionCode())) {
                Log.d("TAG", "紧急开关状态通知: ");
                //紧急开关状态通知
                EventBus.getDefault().post(new EmergencyStatus(buffer, (int) buffer[4]));
            } else if (functionCodeHex.equals(BoobaseCMD.SENSOR_STATUS.getFunctionCode())) {
                //传感器的状态通知
                EventBus.getDefault().post(new SensorStatus(buffer, (int) buffer[4], (int) buffer[5], (int) buffer[6], (int) buffer[7], (int) buffer[8]));
            } else {
                //其他
            }

        }
    }


    /**
     * 开始发送控制命令到串口
     */
    private void startSendCmds() {
        Log.d(TAG, "controlCode: " + controlCode);
        serialPortUtil.sendCmds(controlCode);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        close();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        Log.d(TAG, "BoobaseController onDestroy");
        close();
        if (instance != null) {
            instance = null;
        }
    }


    /**
     * 关闭串口
     */
    private void close() {

        if (serialPortUtil != null) {
            serialPortUtil.closeSerialPort();
            serialPortUtil = null;
        }
    }


    /**
     * 停止移动
     */
    @Override
    public void stopMove() {
        if (controlCodeFlag != FLAG_STOP_MOVE) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.MOVE_STOP.getFunctionCode());
            controlCodeFlag = FLAG_STOP_MOVE;
        }
        instance.startSendCmds();
    }

    /**
     * 向前移动
     */
    @Override
    public void moveForward() {
        if (controlCodeFlag != FLAG_MOVE_FORWARD) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(MoveType, Math.abs(BooBaseController.velocity), 0, yaw);
            controlCodeFlag = FLAG_MOVE_FORWARD;
        }
        instance.startSendCmds();
    }

    /**
     * 向后移动
     */
    @Override
    public void moveBackward() {
        if (controlCodeFlag != FLAG_MOVE_BACKWARD) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(MoveType, -Math.abs(BooBaseController.velocity), 0, yaw);
            controlCodeFlag = FLAG_MOVE_BACKWARD;
        }
        instance.startSendCmds();
    }


    /**
     * 向左转90度
     */
    @Override
    public void turnLeft() {
        if (controlCodeFlag != FLAG_TURN_LEFT) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(MoveType, 0, 0, (float) (Math.PI / 2));
            controlCodeFlag = FLAG_TURN_LEFT;
        }
        instance.startSendCmds();
    }

    /**
     * 向右转90度
     */
    @Override
    public void turnRight() {
        if (controlCodeFlag != FLAG_TURN_RIGHT) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(MoveType, 0, 0, -(float) (Math.PI / 2));
            controlCodeFlag = FLAG_TURN_RIGHT;
        }
        instance.startSendCmds();
    }


    /**
     * 导航命令，自动路径规划和避障
     *
     * @param x   目的点的X坐标
     * @param y   目的点的Y坐标
     * @param raw 到达目的点头朝向的角度
     */
    @Override
    public void navigationTo(float x, float y, float raw) {
        //带参数控制方法标志位必须加上能代表唯一的参数，否则参数改变也认为是同上一个命令不重新生成控制码
        if (controlCodeFlag != FLAG_NAVIGATION_TO + (int)(x * 10000)+(int)(y * 1000) + (int)(raw * 100)) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.NAVIGATION.getFunctionCode(), x, y, yaw);
            controlCodeFlag = FLAG_NAVIGATION_TO+ (int)(x * 10000)+(int)(y * 1000) + (int)(raw * 100);
        }
        instance.startSendCmds();
    }

    /**
     * 原地旋转到指定的角度
     *
     * @param rot 相对当前位置旋转角度弧度，正数为逆时针旋转
     */
    @Override
    public void rotationTo(float rot) {
        //带参数控制方法标志位必须加上能代表唯一的参数，否则参数改变也认为是同上一个命令不重新生成控制码
        if (controlCodeFlag != FLAG_ROTATION_TO+ (int)(rot * 100)) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.ROTATION.getFunctionCode(), rot);
            controlCodeFlag = FLAG_ROTATION_TO+ (int)(rot * 100);
        }
        instance.startSendCmds();
    }


    /**
     * 通过索引导航到某个常用位置（POI） 。常用位置在系统中按顺序保存，索引从 0 开始。
     *
     * @param index 位置索引
     */
    @Override
    public void poiTo(int index) {
        //带参数控制方法标志位必须加上能代表唯一的参数，否则参数改变也认为是同上一个命令不重新生成控制码
        if (controlCodeFlag != FLAG_POI_BY_INDEX+index*100) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.POI_BY_INDEX.getFunctionCode(), index);
            controlCodeFlag = FLAG_POI_BY_INDEX+index*100;
        }
        instance.startSendCmds();
    }

    @Override
    public void poiTo(String name) {
        if (controlCodeFlag != FLAG_POI_BY_NAME+ name.hashCode()) {
            try {
                controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.POI_BY_NAME.getFunctionCode(), new MyInteger(name.getBytes("utf-8").length), name);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            controlCodeFlag = FLAG_POI_BY_NAME+ name.hashCode();
        }
        instance.startSendCmds();
    }

    /**
     * 随机漫游，在所有常用位置(POI)中随机选择目标连续导航
     *
     * @param sleepTime 每次到达一个位置后停留时间，如果为负数，则一直等待继续
     */
    @Override
    public void travelRandom(float sleepTime) {
        //带参数控制方法标志位必须加上能代表唯一的参数，否则参数改变也认为是同上一个命令不重新生成控制码
        if (controlCodeFlag != FLAG_TRAVEL_RANDOM+(int)(sleepTime * 100)) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.TRAVEL_RANDOM.getFunctionCode(), sleepTime);
            controlCodeFlag = FLAG_TRAVEL_RANDOM+(int)(sleepTime * 100);
        }
        instance.startSendCmds();
    }


    /**
     * 顺序漫游，所有常用位置(POI)按照先后顺序逐一导航
     *
     * @param sleepTime 每次到达一个位置后停留时间(秒)，如果为负数，则一直等待直到继续漫游命令触发下一次漫游
     * @param loop      是否开启循环
     */
    @Override
    public void travelOrdinal(float sleepTime, boolean loop) {
        int loopFlag = loop ? 1 : 0;
        if (controlCodeFlag != FLAG_TRAVEL_ORDINAL+(int) (sleepTime*1000)+loopFlag*100) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.TRAVEL_ORDINAL.getFunctionCode(), loopFlag, sleepTime);
            controlCodeFlag = FLAG_TRAVEL_ORDINAL+(int) (sleepTime*1000)+loopFlag*100;
        }
        instance.startSendCmds();
    }

    /**
     * 继续漫游
     */
    @Override
    public void travelContinue() {
        if (controlCodeFlag != FLAG_TRAVEL_CONTINUE) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.TRAVEL_CONTINUE.getFunctionCode());
            controlCodeFlag = FLAG_TRAVEL_CONTINUE;
        }
        instance.startSendCmds();
    }


    /**
     * 停止漫游
     */
    @Override
    public void travelStop() {
        if (controlCodeFlag != FLAG_TRAVEL_STOP) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.TRAVEL_RANDOM.getFunctionCode(), -1f);
            controlCodeFlag = FLAG_TRAVEL_STOP;

        }
        instance.startSendCmds();
    }

    /**
     * 开始充电。当机器人位于充电点附近时，用此命令来启动对接充电。该命令没有导航功能，需要用其它导航相关命令将机器人移到充电点附近，并且满足充电条件
     */
    @Override
    public void chargeStart() {
        if (controlCodeFlag != FLAG_CHARGE_START) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.CHARGE_START.getFunctionCode());
            controlCodeFlag = FLAG_CHARGE_START;
        }
        instance.startSendCmds();
    }

    /**
     * 取消充电。机器人处于充电状态时，使用该命令脱离充电座。
     */
    @Override
    public void chargeCancel() {
        if (controlCodeFlag != FLAG_CHARGE_CANCEL) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.CHARGE_CANCEL.getFunctionCode());
            controlCodeFlag = FLAG_CHARGE_CANCEL;
        }
        instance.startSendCmds();
    }


    /**
     * 连接wifi
     * @param ssid
     * @param password
     */
    @Override
    public void configureWIFI(String ssid, String password) {
        if (controlCodeFlag != FLAG_CONFIG_WIFI+ssid.hashCode()*10+password.hashCode()) {
            try {
                controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.CONFIGURE_WIFI.getFunctionCode(), new MyInteger(ssid.getBytes("utf-8").length),new MyInteger(password.getBytes("utf-8").length), ssid,password);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            controlCodeFlag = FLAG_CONFIG_WIFI+ssid.hashCode()*10+password.hashCode();
        }
        instance.startSendCmds();
    }


}
