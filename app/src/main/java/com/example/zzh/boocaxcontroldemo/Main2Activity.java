package com.example.zzh.boocaxcontroldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.boobasedriver2.RobotManager;
import com.example.boobasedriver2.boobase.BoobaseControllor;
import com.example.boobasedriver2.boobase.event.ChargeStatus;
import com.example.boobasedriver2.boobase.event.EmergencyStatus;
import com.example.boobasedriver2.boobase.event.MoveStatus;
import com.example.boobasedriver2.utils.SharePreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import robot.boocax.com.sdkmodule.entity.entity_sdk.for_app.ReconnReason;
import robot.boocax.com.sdkmodule.entity.entity_sdk.from_server.All_robot_infos;
import robot.boocax.com.sdkmodule.entity.entity_sdk.from_server.OBD;

/**
 * create by zzh on 2018/10/22
 */
public class Main2Activity extends BaseActivity {
    private EditText et_ssid, et_psd, et_x, et_y, et_yaw, et_name;
    private TextView textView, tv_emergy, tv_energy, tv_charge;
    private FloatingActionButton bt_set;
    private MyHandler myHandler = new MyHandler(this);
    ;

    private static class MyHandler extends Handler {
        WeakReference<Main2Activity> mActivityReference;
        Main2Activity activity;

        MyHandler(Main2Activity activity) {
            mActivityReference = new WeakReference<Main2Activity>(activity);
            this.activity = mActivityReference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //电量状态
                    activity.tv_energy.setText((String) msg.obj);
                    break;
                case 1:
                    //急停状态改变
                    Log.d("TAG", "handleMessage1: " + msg.obj);
                    activity.tv_emergy.setText((String) msg.obj);
                    break;
                case 2:
                    //充电状态改变
                    activity.tv_charge.setText((String) msg.obj);
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        EventBus.getDefault().register(this);
        initViews();
        initData();
        RobotManager.createInstance(this);
//        Log.d("TAG", "sdcardPath: " + Environment.getExternalStorageDirectory().getPath() + PathManager.CONFIGURATION_PATH);
//        String s = FileUtil.readFileFromSDCard(PathManager.CONFIGURATION_PATH , "test.cfg");
//        Log.d("TAG", "sd : "+s);
        BoobaseTcpConnector.createInstance(this);


    }

    private void initData() {
        String boobaseCom = (String) SharePreferenceUtils.getParam(getApplicationContext(), "boobase", "");
        String boobaseRate = (String) SharePreferenceUtils.getParam(getApplicationContext(), "boobaseRate", "");

        if (!TextUtils.isEmpty(boobaseCom)) {
            BoobaseControllor.boobaseCom = boobaseCom;
        }
        if (!TextUtils.isEmpty(boobaseRate)) {
            BoobaseControllor.boobaseRate = Integer.parseInt(boobaseRate);
        }
    }


    private void initViews() {
        et_ssid = findViewById(R.id.et_ssid2);
        et_psd = findViewById(R.id.et_pwd2);
        et_x = findViewById(R.id.et_x2);
        et_y = findViewById(R.id.et_y2);
        et_yaw = findViewById(R.id.et_raw2);
        et_name = findViewById(R.id.et_name2);
        textView = findViewById(R.id.tv2);
        tv_emergy = findViewById(R.id.tv_emergy);
        tv_energy = findViewById(R.id.tv_energy);
        tv_charge = findViewById(R.id.tv_charge);
        bt_set = findViewById(R.id.set);
        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, SettingActivty.class));
            }
        });
    }

    public void connect_wifi(View view) {
        String ssid = et_ssid.getText().toString();
        String psd = et_psd.getText().toString();
        if (!TextUtils.isEmpty(ssid) && !TextUtils.isEmpty(psd))
            RobotManager.getControl().connectWifi(ssid, psd);
    }

    public void moveForward(View view) {
        RobotManager.getControl().moveForward();
    }

    public void moveBackward(View view) {
        RobotManager.getControl().moveBackward();
    }

    public void turnleft(View view) {
        RobotManager.getControl().turnLeft();
    }

    public void turnright(View view) {
        RobotManager.getControl().turnRight();
    }

    public void travelOrdinal(View view) {

    }

    public void travelRadom(View view) {
    }

    public void travelStop(View view) {
    }

    public void travelContinue(View view) {
    }

    public void naviTo(View view) {
        String xStr = et_x.getText().toString();
        String yStr = et_y.getText().toString();
        String rawStr = et_yaw.getText().toString();
        if (!TextUtils.isEmpty(xStr) && !TextUtils.isEmpty(yStr) && !TextUtils.isEmpty(rawStr)) {
            float x = Float.parseFloat(xStr);
            float y = Float.parseFloat(yStr);
            float raw = Float.parseFloat(rawStr);
            RobotManager.getControl().navigationTo(x, y, raw);
        }
    }

    public void naviName(View view) {
        String name = et_name.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            RobotManager.getControl().navigationTo(name);
        }
    }

    public void poiIndex(View view) {

    }


    public void stop(View view) {
        RobotManager.getControl().stopMove();
    }

    public void startActivity(View view) {
        startActivity(new Intent(this, Main3Activity.class));
    }

    public void startCharge(View view) {
        RobotManager.getControl().chargeStart();
    }

    public void stopCharge(View view) {
        RobotManager.getControl().chargeCancel();
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void move(final MoveStatus moveStatus) {
        Log.d("TAG", "move: " + moveStatus.getMsg());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append("moveStatus:" + moveStatus.getMsg() + " ");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BoobaseTcpConnector.getInstance() != null) {
            BoobaseTcpConnector.getInstance().destoryConnector();
        }
        EventBus.getDefault().unregister(this);
    }

    public void rotation(View view) {
        if (TextUtils.isEmpty(et_yaw.getText().toString())) return;
        int angle = Integer.parseInt(et_yaw.getText().toString());
        RobotManager.getControl().rotationTo(angle);
    }

    public void clear(View view) {
        textView.setText("");
    }

    //串口
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEmergencyStatusReciver(EmergencyStatus status) {
        setEnergency(status.getMsg());
    }


    private void setEnergency(int status) {
        String text = "";
        switch (status) {
            case 0:
            case 1:
                text = "抬起";
                break;
            case 2:
                text = "按下";
                break;
        }
        Message message = new Message();
        message.what = 1;
        message.obj = text;
        myHandler.sendMessage(message);
    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onChargeStatusReciver(ChargeStatus status) {
        setChangeStatus(status.getMsg());
    }


    private void setChangeStatus(int status) {
        String text = "";
        switch (status) {
            case ChargeStatus.BOOCAX_CHARGE_UNCHARGED:
                text = "未充电";
                break;
            case ChargeStatus.BOOCAX_CHARGE_CHARGEING_TO_BOOBASE:
                text = "正在充电";
                break;
            case ChargeStatus.BOOCAX_CHARGE_FULL:
                text = "充电完成";
                break;
            default:
                text = "正在充电";
                break;
        }

        if (!text.equals(tv_charge.getText().toString())) {
            Message message = new Message();
            message.what = 2;
            message.obj = text;
            myHandler.sendMessage(message);
        }
    }


//    public void starttcp(View view) {
//        startActivity(new Intent(this, NetControlActivity.class));
//    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void getOBD(OBD obd) {
        if (obd != null) {
            String obdStr = obd.getObd().split(" ")[5];
            if (!obdStr.equals(tv_energy.getText().toString())) {
                Message message = new Message();
                message.what = 0;
                message.obj = obdStr;
                myHandler.sendMessage(message);
            }

        }
    }

    /**
     * 收到新传来的allRobotInfo
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void getAllRobotInfos(All_robot_infos.AllRobotInfoBean all_robot_info) {
        if (all_robot_info != null && all_robot_info.getMac_address() != null) {
            int energencyStatus = all_robot_info.getButton_status().getEmergency_button();
            setEnergency(energencyStatus);

            int charge_status = all_robot_info.getCharge_status();
            setChangeStatus(charge_status);


        }
    }


}
