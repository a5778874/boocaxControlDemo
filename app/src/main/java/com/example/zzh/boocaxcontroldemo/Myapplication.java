package com.example.zzh.boocaxcontroldemo;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.boobasedriver2.boobase.BoobaseController;
import com.example.boobasedriver2.boobase.BoobaseService;
import com.squareup.leakcanary.LeakCanary;

/**
 * create by zzh on 2018/10/22
 */
public class Myapplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        BoobaseController.regist(this);

    }
}
