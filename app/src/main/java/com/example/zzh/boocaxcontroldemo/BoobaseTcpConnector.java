package com.example.zzh.boocaxcontroldemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;

import robot.boocax.com.sdkmodule.TCP_CONN;
import robot.boocax.com.sdkmodule.entity.entity_app.LoginEntity;
import robot.boocax.com.sdkmodule.entity.entity_sdk.for_app.UDPList;
import robot.boocax.com.sdkmodule.utils.init_files.NaviContext;

import static android.content.Context.MODE_PRIVATE;

/**
 * create by zzh on 2018/12/10
 */
public class BoobaseTcpConnector {
    private String robotName = "robot-guoji0001-SERVER";


    private static BoobaseTcpConnector instance;
    private Context context;

    public static SharedPreferences sp_curDoc;
    public static SharedPreferences.Editor editor_curDoc;//用于记录文件(服务器传来)


    private BoobaseTcpConnector(Context context) {
        this.context = context;
        initContext();                                      //同步app和SDK环境;
        instanceData();                                     //定义SDK接收文件种类.
        EventBus.getDefault().register(this);               //注册EventBus
        startUDP();                                         //开启UDP
    }

    public static synchronized BoobaseTcpConnector createInstance(Context context) {
        //初始化串口

        if (instance == null) {
            instance = new BoobaseTcpConnector(context);
        }
        return instance;
    }


    public static BoobaseTcpConnector getInstance() {
        return instance;
    }


    /**
     * 同步app与SDK环境变量
     */
    private void initContext() {
        if (NaviContext.context == null) {
            NaviContext.context = context.getApplicationContext();
        }
        //创建SharedPreferences,用于记录文件清单
        sp_curDoc = context.getSharedPreferences("recordDoc", MODE_PRIVATE);
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
        //LoginEntity.recvFileTypes.add("map.png");
        // LoginEntity.recvFileTypes.add("restrict.dat");
//        LoginEntity.recvFileTypes.add("anchor.dat");
        LoginEntity.recvFileTypes.add("poi.json");
//        LoginEntity.recvFileTypes.add("agv_graph.json");
        //定义Android客户端接收的文件类型,SDK使用者根据自身客户端功能选择需要接收的文件
    }

    //开启UDP
    public void startUDP() {
        Log.d("TAG5", "startUDP: ");
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
        if (udpList != null) {
            Log.e("TAG5", "getUDPInfo: " + udpList.toString());
            String serverName = udpList.getList().get(0);
            if (robotName.equals(serverName)) {
                connectTCP(udpList);
            }

        }
    }


    //搜索到开始连接
    private void connectTCP(UDPList udpList) {
        LoginEntity.serverName = udpList.getList().get(0);
        LoginEntity.serverIP = udpList.getList().get(1);                                                 //获取serverIP
        String isLock = udpList.getList().get(2);//获取是否加锁
        Log.d("TAG5", "connecting TCP: " + LoginEntity.serverName + "..." + LoginEntity.serverIP);
        TCP_CONN.isUDP = true;//关闭UDP
        TCP_CONN.loopMark = true;//开启TCP主连接


        if ("false".equals(isLock)) {                   //无密码登录
            TCP_CONN.isSendReconn = true;               //是否开启重连
            TCP_CONN.loopMark = true;                   //开启TCP主循环;
            context.startService(new Intent(context, MyService_verify.class));
        } else if ("true".equals(isLock)) {              //有密码登录
            Log.e("TAG5", "connectTCP:需要密码 ");
        }
    }


    public void destoryConnector() {
        exit();
        EventBus.getDefault().unregister(this);
        Log.d("TAG5", "destoryConnector: ");
    }

    //退出
    private void exit() {
        TCP_CONN.isSendReconn = false;
        TCP_CONN.loopMark = false;
        if (TCP_CONN.channel != null) {
            try {
                TCP_CONN.channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            TCP_CONN.channel = null;
        }
        if (TCP_CONN.key != null) {
            TCP_CONN.key.cancel();
            TCP_CONN.key = null;
        }
        if (TCP_CONN.selector != null) {
            TCP_CONN.selector = null;
        }
        Intent intent = new Intent(context, MyService_verify.class);
        context.stopService(intent);
    }


}
