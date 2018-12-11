package com.example.boobasedriver2.boobase;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.boobasedriver2.boobase.event.BoobaseSensor;
import com.example.boobasedriver2.boobase.event.ChargeStatus;
import com.example.boobasedriver2.boobase.event.EmergencyStatus;
import com.example.boobasedriver2.boobase.event.LocationStatus;
import com.example.boobasedriver2.boobase.event.MoveStatus;
import com.example.boobasedriver2.boobase.event.SensorStatus;
import com.example.boobasedriver2.boobase.event.WifiConfigureStatus;
import com.example.boobasedriver2.boobase.inter.IControllor;
import com.example.boobasedriver2.utils.FileUtil;
import com.example.boobasedriver2.utils.MyInteger;
import com.example.boobasedriver2.utils.PathManager;
import com.gjdl.common.thread.CachedTreadPoolOperator;
import com.iflytek.aiui.uartkit.util.SerialDataUtils;
import com.iflytek.aiui.uartkit.util.SerialPortUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


/**
 * create by zzh on 2018/10/15
 */
public class BoobaseControllor extends BaseControllor implements IControllor {

    private static BoobaseControllor instance;
    private SerialPortUtil serialPortUtil;
    private float defalutSpeed = 0.37f; // 默认底盘移动的速度 0.37
    private String defalutMoveType = BoobaseCMD.SAFE_MOVE.getFunctionCode();   //默认为安全移动
    private String controlCode = "";//按协议生成的控制命令
    private int controlCodeFlag = -1;  //控制标记，记录上一次操作的控制命令,每个命令的控制标记都是不重复的，避免重复调用而生成相同的控制码

    public static String boobaseCom = SerialPortUtil.SERIAL_PORT_COM1;
    public static int boobaseRate = SerialPortUtil.SERIAL_BAUDRATE_9600;

    private BoobaseControllor() {
        initBoobaseConfig();
        initSerial();
    }


    public static synchronized BoobaseControllor createInstance() {
        //初始化串口

        if (instance == null) {
            instance = new BoobaseControllor();
        }
        return instance;
    }

    //得到控制器
    public static synchronized BoobaseControllor getControl() {
        return instance;
    }

    private void initBoobaseConfig() {
        try {
            //读取配置文件Boobase.cfg中的默认配置速度信息，和移动模式
            String fileName = "Boobase.cfg";
//            String cfg = FileUtil.readAssetsFile(getApplicationContext(), configPath);
            String cfg = FileUtil.readFileFromSDCard(PathManager.CONFIGURATION_PATH, fileName);
            if (TextUtils.isEmpty(cfg)) {
                //todo 配置文件不存在
                Log.e("TAG", "Boobase.cfg 不存在 ");
                return;
            }
            Log.d("TAG", "initBoobaseConfig Boobase.cfg: \n " + cfg);
            JSONObject jsonObject = new JSONObject(cfg);
            defalutSpeed = (float) jsonObject.optDouble("speed");
            int moveType = jsonObject.optInt("moveType");
            //moveType=0为安全移动，1为强制移动
            defalutMoveType = (moveType == 0) ? BoobaseCMD.SAFE_MOVE.getFunctionCode() : BoobaseCMD.FORCEDLY_MOVE.getFunctionCode();

        } catch (Exception e) {
            //todo 配置文件不存在
            Log.e("TAG", "Boobase.cfg 配置有误 ");
        }

    }


    private void initSerial() {
        //Log.d("TAG1", "initSerial: "+boobaseCom+","+boobaseRate);
        serialPortUtil = SerialPortUtil.getInstance(boobaseCom, boobaseRate);
        serialPortUtil.setOnDataReceiveListener(new SerialPortUtil.OnDataReceiveListener() {
            @Override
            public void onDataReceive(byte[] buffer, int size) {
                Log.d("TAG1", "boobase串口接收: " + SerialDataUtils.ByteArrToHex(buffer));
                postStatus(buffer);
            }
        });
    }

    private byte head1 = SerialDataUtils.HexToByte("AA");
    private byte head2 = SerialDataUtils.HexToByte("55");

    private void postStatus(byte[] buffer) {
        if (buffer[0] == head1 && buffer[1] == head2) {
            String functionCodeHex = SerialDataUtils.Byte2Hex(buffer[3]);
            Log.d("TAG2", "functionCodeHex: "+functionCodeHex+"..."+BoobaseCMD.EMERGENCY_STATUS.getFunctionCode());
            if (functionCodeHex.equals(BoobaseCMD.MOVE_STATUS.getFunctionCode())) {
                //移动状态通知
                EventBus.getDefault().post(new MoveStatus((int) buffer[4]));
            } else if (functionCodeHex.equals(BoobaseCMD.LOCATION_STATUS.getFunctionCode())) {
                //位置状态通知
                EventBus.getDefault().post(new LocationStatus((int) buffer[4]));
            } else if (functionCodeHex.equals(BoobaseCMD.CHARGE_STATUS.getFunctionCode())) {
                //充电状态通知
                EventBus.getDefault().post(new ChargeStatus((int) buffer[4]));
            } else if (functionCodeHex.equals(BoobaseCMD.EMERGENCY_STATUS.getFunctionCode())) {
                //紧急开关状态通知
                Log.d("TAG2", "紧急开关状态通知: ");
                EventBus.getDefault().post(new EmergencyStatus((int) buffer[4]));
            } else if (functionCodeHex.equals(BoobaseCMD.CONFIGURE_WIFI.getFunctionCode())) {
                //配置wifi状态通知
                EventBus.getDefault().post(new WifiConfigureStatus((int) buffer[4]));
            } else if (functionCodeHex.equals(BoobaseCMD.SENSOR_STATUS.getFunctionCode())) {
                //传感器的状态通知
                boolean uwbErrorStatus = (int) buffer[4] != 0;
                boolean laserErrorStatus = (int) buffer[5] != 0;
                boolean boobaseErrorStatus = (int) buffer[6] != 0;
                boolean encodingDiskErrorStatus = (int) buffer[7] != 0;
                boolean cameraErrorStatus = (int) buffer[8] != 0;
                BoobaseSensor boobaseSensor = new BoobaseSensor(uwbErrorStatus, laserErrorStatus, boobaseErrorStatus, encodingDiskErrorStatus, cameraErrorStatus);
                SensorStatus sensorStatus = new SensorStatus(boobaseSensor);
                EventBus.getDefault().post(sensorStatus);
            } else {
                //其他
            }

        }
    }


    /**
     * 开始发送控制命令到串口
     */
    private void sendCmds() {
        CachedTreadPoolOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                serialPortUtil.sendCmds(controlCode);
                Log.d("TAG", "sendCmds: " + controlCode + "..thread:" + Thread.currentThread().getName());
            }
        });
    }

    /**
     * 关闭串口
     */
    public void close() {
        if (serialPortUtil != null)
            serialPortUtil.closeSerialPort();

    }


    //region 底盘控制

    /**
     * 停止移动
     */
    @Override
    public void stopMove() {
        int mFlag = generalFlag("stopMove");
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.MOVE_STOP.getFunctionCode());
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == stopMove.... ");
        }
        sendCmds();
    }

    /**
     * 向前移动
     */
    @Override
    public void moveForward() {
        int mFlag = generalFlag("moveForward");
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(defalutMoveType, Math.abs(defalutSpeed), 0, 0);
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == moveForward.... ");
        }
        sendCmds();
    }

    /**
     * 向后移动
     */
    @Override
    public void moveBackward() {
        int mFlag = generalFlag("moveBackward");
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(defalutMoveType, -Math.abs(defalutSpeed), 0, 0);
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == moveBackward.... ");
        }
        sendCmds();
    }


    /**
     * 向左转90度
     */
    @Override
    public void turnLeft() {
        int mFlag = generalFlag("turnLeft");
        if (controlCodeFlag != mFlag) {
            //controlCode = BoobaseCommandConverter.getInstance().convertProtocols(defalutMoveType, 0, 0, (float) (Math.PI / 2));
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.ROTATION.getFunctionCode(), (float) (Math.PI / 2));
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == turnLeft.... ");
        }
        sendCmds();
    }

    /**
     * 向右转90度
     */
    @Override
    public void turnRight() {
        int mFlag = generalFlag("turnRight");
        if (controlCodeFlag != mFlag) {
            //controlCode = BoobaseCommandConverter.getInstance().convertProtocols(defalutMoveType, 0, 0, -(float) (Math.PI / 2));
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.ROTATION.getFunctionCode(), -(float) (Math.PI / 2));
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == turnRight.... ");
        }
        sendCmds();
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
        int mFlag = generalFlag("navigationTo", x, y, raw);
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.NAVIGATION.getFunctionCode(), x, y, 0);
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == navigationTo(" + x + "," + y + "," + raw + ")");
        }
        sendCmds();
    }

    /**
     * 原地旋转到指定的角度
     *
     * @param rot 相对当前位置旋转角度弧度，正数为逆时针旋转
     */
    @Override
    public void rotationTo(float rot) {
        int mFlag = generalFlag("rotationTo", rot);
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.ROTATION.getFunctionCode(), rot);
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == rotationTo:" + rot);
        }
        sendCmds();
    }


    /**
     * 通过索引导航到某个常用位置（POI） 。常用位置在系统中按顺序保存，索引从 0 开始。
     *
     * @param index 位置索引
     */
    @Override
    public void poiTo(int index) {
        int mFlag = generalFlag("poiTo", index);
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.POI_BY_INDEX.getFunctionCode(), index);
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == poiTo Index:" + index);
        }
        sendCmds();
    }


    /**
     * 通过名字
     * <p>
     * 导航到某个常用位置（POI）
     *
     * @param name 点名称
     */
    @Override
    public void poiTo(String name) {
        int mFlag = generalFlag("poiTo", name);
        if (controlCodeFlag != mFlag) {
            try {
                controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.POI_BY_NAME.getFunctionCode(), new MyInteger(name.getBytes("utf-8").length), name);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == poiToName: " + name);
        }
        sendCmds();
    }

    /**
     * 随机漫游，在所有常用位置(POI)中随机选择目标连续导航
     *
     * @param sleepTime 每次到达一个位置后停留时间，如果为负数，则一直等待继续
     */
    @Override
    public void travelRandom(float sleepTime) {
        int mFlag = generalFlag("travelRandom", sleepTime);
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.TRAVEL_RANDOM.getFunctionCode(), sleepTime);
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == travelRandom.. ");
        }
        sendCmds();
    }


    /**
     * 顺序漫游，所有常用位置(POI)按照先后顺序逐一导航
     *
     * @param sleepTime 每次到达一个位置后停留时间(秒)，如果为负数，则一直等待直到继续漫游命令触发下一次漫游
     * @param loop      是否开启循环
     */
    @Override
    public void travelOrdinal(float sleepTime, boolean loop) {
        int mFlag = generalFlag("travelOrdinal", sleepTime, loop);
        int loopFlag = loop ? 1 : 0;
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.TRAVEL_ORDINAL.getFunctionCode(), loopFlag, sleepTime);
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == travelOrdinal.. ");
        }
        sendCmds();
    }

    /**
     * 继续漫游
     */
    @Override
    public void travelContinue() {
        int mFlag = generalFlag("travelContinue");
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.TRAVEL_CONTINUE.getFunctionCode());
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == travelContinue... ");
        }
        sendCmds();
    }


    /**
     * 停止漫游
     */
    @Override
    public void travelStop() {
        int mFlag = generalFlag("travelStop");
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.TRAVEL_RANDOM.getFunctionCode(), -1f);
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == travelStop.... ");
        }
        sendCmds();
    }

    /**
     * 开始充电。当机器人位于充电点附近时，用此命令来启动对接充电。该命令没有导航功能，需要用其它导航相关命令将机器人移到充电点附近，并且满足充电条件
     */
    @Override
    public void chargeStart() {
        int mFlag = generalFlag("chargeStart");
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.CHARGE_START.getFunctionCode());
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == chargeStart.... ");
        }
        sendCmds();
    }

    /**
     * 取消充电。机器人处于充电状态时，使用该命令脱离充电座。
     */
    @Override
    public void chargeCancel() {
        int mFlag = generalFlag("chargeCancel");
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.CHARGE_CANCEL.getFunctionCode());
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == chargeCancel.... ");
        }
        sendCmds();
    }


    /**
     * 接入wifi
     *
     * @param ssid     要接入的wifi名字
     * @param password wifi密码
     */
    @Override
    public void configureWIFI(String ssid, String password) {
        int mFlag = generalFlag("configureWIFI", ssid, password);
        if (controlCodeFlag != mFlag) {
            try {
                controlCode = BoobaseCommandConverter.getInstance().convertProtocols(BoobaseCMD.CONFIGURE_WIFI.getFunctionCode(), new MyInteger(ssid.getBytes("utf-8").length), new MyInteger(password.getBytes("utf-8").length), ssid, password);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == configureWIFI ssid:" + ssid + "  psd:" + password);
        }
        sendCmds();
    }
    //endregion


}
