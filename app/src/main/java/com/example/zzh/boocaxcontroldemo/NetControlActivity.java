package com.example.zzh.boocaxcontroldemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import robot.boocax.com.sdkmodule.TCP_CONN;
import robot.boocax.com.sdkmodule.entity.entity_app.LoginEntity;
import robot.boocax.com.sdkmodule.entity.entity_sdk.for_app.UDPList;

/**
 * create by zzh on 2018/8/9
 */
public class NetControlActivity extends BaseActivity {
    /**
     * SDK内容
     */
    public static SharedPreferences sp_curDoc;
    public static SharedPreferences.Editor editor_curDoc;//用于记录文件(服务器传来)

    private final String TAG = "55555";
    private String robotname="robot-guoji0001-SERVER";
    private String serIP =null;
    LoginEntity loginEntity=new LoginEntity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netcontrol);
        EventBus.getDefault().register(this);
    }

    public void startUDP() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TCP_CONN.isUDP = false;
                TCP_CONN.getUDPs();
            }
        }).start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUDPInfo(UDPList udpList) {

        if (udpList != null) {
            Log.i(TAG, "SearchServer==>" + udpList.getList().toString());
            String serverName = udpList.getList().get(0);
            String serverIP = udpList.getList().get(1);
            String displayStr = serverName + "" + serverIP;

        }
    }


    class KEEPCONN implements Runnable {
        @Override
        public void run() {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void connect(View v) {
        startUDP();
    }

}
