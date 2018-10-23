package com.example.boobasedriver2.boobase;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.boobasedriver2.boobase.inter.IControllor;
import com.example.boobasedriver2.utils.FileUtil;
import com.example.boobasedriver2.utils.MyInteger;
import com.gjdl.common.thread.CachedTreadPoolOperator;
import com.iflytek.aiui.uartkit.util.SerialPortUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


/**
 * create by zzh on 2018/10/15
 */
public class BoobaseService extends Service {


    private SerialPortUtil serialPortUtil;
    private float defalutSpeed = 0.37f; // 默认底盘移动的速度 0.37
    private String defalutMoveType = BoobaseCMD.SAFE_MOVE.getFunctionCode();   //默认为安全移动
    private String controlCode = "";//按协议生成的控制命令
    private int controlCodeFlag = -1;  //控制标记，记录上一次操作的控制命令,每个命令的控制标记都是不重复的，避免重复调用而生成相同的控制码

    //region 服务绑定相关

    //底盘控制器回调
    public interface BindServiceListener {
        void ServiceConnected(IControllor boobaseControllor);
    }

    public static void bind(Context context, final BindServiceListener bindServiceListener) {

        Intent intent = new Intent(context, BoobaseService.class);
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (bindServiceListener != null) {
                    bindServiceListener.ServiceConnected((IControllor) service);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);

    }
    //endregion


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "BoobaseService onCreate:... ");
        serialPortUtil = SerialPortUtil.getInstance(SerialPortUtil.SERIAL_PORT_COM1, SerialPortUtil.SERIAL_BAUDRATE_9600);
        initBoobaseConfig();
    }


    private void initBoobaseConfig() {
        try {
            //读取配置文件Boobase.cfg中的默认配置速度信息，和移动模式
            String configPath = "cfg/Boobase.cfg";
            String cfg = FileUtil.readAssetsFile(getApplicationContext(), configPath);
            JSONObject jsonObject = new JSONObject(cfg);
            defalutSpeed = (float) jsonObject.optDouble("speed");
            int moveType = jsonObject.optInt("moveType");
            //moveType=0为安全移动，1为强制移动
            defalutMoveType = (moveType == 0) ? BoobaseCMD.SAFE_MOVE.getFunctionCode() : BoobaseCMD.FORCEDLY_MOVE.getFunctionCode();

        } catch (Exception e) {
            e.printStackTrace();
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
                Log.d("TAG", "sendCmds: " + controlCode+"..thread:"+Thread.currentThread().getName());
            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("TAG", "onbind:.... ");
        return new BoobaseControllor();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        serialPortUtil.closeSerialPort();
        Log.d("TAG", "BoobaseService onDestroy:... ");
    }


    class BoobaseControllor extends Binder implements IControllor {

        @Override
        public void moveForward() {
            BoobaseService.this.moveForward();
        }

        @Override
        public void moveBackward() {
            BoobaseService.this.moveBackward();
        }

        @Override
        public void turnLeft() {
            BoobaseService.this.turnLeft();
        }

        @Override
        public void turnRight() {
            BoobaseService.this.turnRight();
        }

        @Override
        public void stopMove() {
            BoobaseService.this.stopMove();
        }

        @Override
        public void navigationTo(float x, float y, float raw) {
            BoobaseService.this.navigationTo(x, y, raw);
        }

        @Override
        public void rotationTo(float rot) {
            BoobaseService.this.rotationTo(rot);
        }

        @Override
        public void poiTo(int index) {
            BoobaseService.this.poiTo(index);
        }

        @Override
        public void poiTo(String name) {
            BoobaseService.this.poiTo(name);
        }

        @Override
        public void travelRandom(float sleepTime) {
            BoobaseService.this.travelRandom(sleepTime);
        }

        @Override
        public void travelOrdinal(float sleepTime, boolean loop) {
            BoobaseService.this.travelOrdinal(sleepTime, loop);
        }

        @Override
        public void travelContinue() {
            BoobaseService.this.travelContinue();
        }

        @Override
        public void travelStop() {
            BoobaseService.this.travelStop();
        }

        @Override
        public void chargeStart() {
            BoobaseService.this.chargeStart();
        }

        @Override
        public void chargeCancel() {
            BoobaseService.this.chargeCancel();
        }

        @Override
        public void configureWIFI(String ssid, String password) {
            BoobaseService.this.configureWIFI(ssid, password);
        }
    }


    //region 底盘控制

    /**
     * 停止移动
     */
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
    public void turnLeft() {
        int mFlag = generalFlag("turnLeft");
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(defalutMoveType, 0, 0, (float) (Math.PI / 2));
            controlCodeFlag = mFlag;
            Log.i("TAG", "BoobaseService == turnLeft.... ");
        }
        sendCmds();
    }

    /**
     * 向右转90度
     */
    public void turnRight() {
        int mFlag = generalFlag("turnRight");
        if (controlCodeFlag != mFlag) {
            controlCode = BoobaseCommandConverter.getInstance().convertProtocols(defalutMoveType, 0, 0, -(float) (Math.PI / 2));
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


    /**
     * 根据控制参数生成独立标记码，用于避免生成同样的控制码
     */
    private static int generalFlag(Object... params) {
        int flag = -1;
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof String) {
                flag += params[i].hashCode();
            } else if (params[i] instanceof Integer) {
                flag += (Integer) params[i] * Math.pow(10, i + 1);
            } else if (params[i] instanceof Float) {
                flag += (float) params[i] * Math.pow(20, i + 1);
            } else if (params[i] instanceof Boolean) {
                flag = (int) ((boolean) params[i] ? flag + Math.pow(30, i + 1) : flag + Math.pow(40, i + 1));
            } else {
                flag += (Integer) params[i] * Math.pow(60, i + 1);
            }
        }
        return flag;
    }




}
