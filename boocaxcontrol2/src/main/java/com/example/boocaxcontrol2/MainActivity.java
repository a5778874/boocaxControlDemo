package com.example.boocaxcontrol2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindService(new Intent(this, BoobaseService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("TAG", "onServiceConnected: ");
                IControllor iControllor= (IControllor) service;
                iControllor.moveForward();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("TAG", "onServiceDisconnected: "+name.toShortString());
            }
        }, BIND_AUTO_CREATE);


    }
}
