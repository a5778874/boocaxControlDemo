package com.example.wificontroldemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import robot.boocax.com.sdkmodule.APPSend;
import robot.boocax.com.sdkmodule.TCP_CONN;
import robot.boocax.com.sdkmodule.entity.entity_app.LoginEntity;
import robot.boocax.com.sdkmodule.entity.entity_sdk.for_app.UDPList;
import robot.boocax.com.sdkmodule.entity.entity_sdk.from_server.Pos_vel_status;
import robot.boocax.com.sdkmodule.utils.init_files.NaviContext;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "9999999999";
    /**
     * SDK内容
     */
    public static SharedPreferences sp_curDoc;
    public static SharedPreferences.Editor editor_curDoc;//用于记录文件(服务器传来)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        startUDP();
        initContext();
        instanceData();
        setReconn();
    }


    //开启UDP
    public void startUDP() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TCP_CONN.isUDP = false;
                TCP_CONN.getUDPs();
            }
        }).start();
    }

    /**
     * UDP广播
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUDPInfo(UDPList udpList) {
        Log.e(TAG, "getUDPList: " + udpList);
        if (udpList != null) {

            String serverName = udpList.getList().get(0);
            String serverIP = udpList.getList().get(1);
            String displayStr = serverName + ":" + serverIP;
            Log.d(TAG, "getUDPInfo: " + displayStr);
            LoginEntity.serverIP = serverIP;                                                 //获取serverIP
            LoginEntity.serverName = serverName;
            TCP_CONN.loopMark = true;//开启TCP主连接
            TCP_CONN.isSendReconn = true;        //是否开启重连
            startService(new Intent(MainActivity.this, MyService_verify.class));
            Log.d(TAG, "getFilesDir: "+getFilesDir().getPath());

        }
    }

    /**
     * 同步app与SDK环境变量
     */
    private void initContext() {
        if (NaviContext.context == null) {
            NaviContext.context = getApplicationContext();
        }
        //创建SharedPreferences,用于记录文件清单
        sp_curDoc = getSharedPreferences("recordDoc", MODE_PRIVATE);
        editor_curDoc = sp_curDoc.edit();
        //获取SharedPreferences地址
        if (TCP_CONN.sp_curDoc == null) {
            TCP_CONN.sp_curDoc = sp_curDoc;
        }
        if (TCP_CONN.editor_curDoc == null) {
            TCP_CONN.editor_curDoc = editor_curDoc;
        }
    }


    /**
     * 定义SDK接收文件种类
     */
    private void instanceData() {
        LoginEntity.recvFileTypes = new ArrayList<>();
//        LoginEntity.recvFileTypes.add("map.png");
//        LoginEntity.recvFileTypes.add("restrict.dat");
//        LoginEntity.recvFileTypes.add("anchor.dat");
        LoginEntity.recvFileTypes.add("poi.json");
        //LoginEntity.recvFileTypes.add("agv_graph.json");
        //定义Android客户端接收的文件类型,SDK使用者根据自身客户端功能选择需要接收的文件

    }


    /**
     * 网络重连参数
     */
    private void setReconn() {
        TCP_CONN.reconnTime = 5000;        //重连时间(不设置默认5000ms,上层可更改,建议3000ms以上).
        TCP_CONN.timeOutCount = 5;          //轮询接收数据 超过该次数 返回超时状态
        TCP_CONN.connOutCount = 10;         //轮询接收数据 超过该次数 返回网络断开 SDK内部进行重连操作.
    }


    /**
     * 速度与位置信息
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void getVelPose(Pos_vel_status pos_vel_status) {
        if (pos_vel_status != null) {
            double poseX = pos_vel_status.getPose().getX();
            double poseY = pos_vel_status.getPose().getY();
            double poseYaw = pos_vel_status.getPose().getYaw();

            double vx = pos_vel_status.getVel().getVx();
            double vy = pos_vel_status.getVel().getVy();
            double vtheta = pos_vel_status.getVel().getVtheta();

            Log.d(TAG, "getVelPose: "+"X:"+poseX+" Y:"+poseY+" YAW:"+poseYaw+" VX:"+vx+" VY:"+vy+" vtheta:"+vtheta);

        }
    }




}
