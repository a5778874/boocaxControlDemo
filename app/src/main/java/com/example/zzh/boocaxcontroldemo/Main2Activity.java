package com.example.zzh.boocaxcontroldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.boobasedriver2.RobotManager;
import com.example.boobasedriver2.boobase.event.MoveStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * create by zzh on 2018/10/22
 */
public class Main2Activity extends BaseActivity {
    private EditText et_ssid, et_psd, et_x, et_y, et_yaw, et_name;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        EventBus.getDefault().register(this);
//        Log.d("TAG", "sdcardPath: " + Environment.getExternalStorageDirectory().getPath() + PathManager.CONFIGURATION_PATH);
//        String s = FileUtil.readFileFromSDCard(PathManager.CONFIGURATION_PATH , "test.cfg");
//        Log.d("TAG", "sd : "+s);
        initViews();
    }


    private void initViews() {
        et_ssid = findViewById(R.id.et_ssid2);
        et_psd = findViewById(R.id.et_pwd2);
        et_x = findViewById(R.id.et_x2);
        et_y = findViewById(R.id.et_y2);
        et_yaw = findViewById(R.id.et_raw2);
        et_name = findViewById(R.id.et_name2);
        textView = findViewById(R.id.tv2);

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
                textView.append(moveStatus.getMsg() + " \n");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void rotation(View view) {
        if (TextUtils.isEmpty(et_yaw.getText().toString())) return;
        int angle = Integer.parseInt(et_yaw.getText().toString());
        RobotManager.getControl().rotationTo(angle);
    }
}
