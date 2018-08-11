package com.example.zzh.boocaxcontroldemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
public class NetControlActivity extends AppCompatActivity{
private  final String TAG="TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netcontrol);
        EventBus.getDefault().register(this);
        startUDP();
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

}
