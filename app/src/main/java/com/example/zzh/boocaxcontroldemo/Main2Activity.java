package com.example.zzh.boocaxcontroldemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.boobasedriver2.boobase.BoobaseController;
import com.example.boobasedriver2.boobase.BoobaseService;
import com.example.boobasedriver2.boobase.event.MoveStatus;
import com.example.boobasedriver2.utils.FileUtil;
import com.example.boobasedriver2.utils.PathManager;
import com.example.zzh.boocaxcontroldemo.Bean.RobotStatus;

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
        et_ssid = findViewById(R.id.et_ssid);
        et_psd = findViewById(R.id.et_psd);
        et_x = findViewById(R.id.et_x);
        et_y = findViewById(R.id.et_y);
        et_yaw = findViewById(R.id.et_raw);
        et_name = findViewById(R.id.et_name);
        textView = findViewById(R.id.tv);

    }

    public void connect_wifi(View view) {
        String ssid = et_ssid.getText().toString();
        String psd = et_psd.getText().toString();
        if (!TextUtils.isEmpty(ssid) && !TextUtils.isEmpty(psd))
            BoobaseController.getControl().connectWifi(ssid, psd);
    }

    public void moveForward(View view) {
        BoobaseController.getControl().moveForward();
    }

    public void moveBackward(View view) {
        BoobaseController.getControl().moveBackward();
    }

    public void turnleft(View view) {
        BoobaseController.getControl().turnLeft();
    }

    public void turnright(View view) {
        BoobaseController.getControl().turnRight();
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
            BoobaseController.getControl().navigationTo(x, y, raw);
        }
    }

    public void naviName(View view) {
        String name = et_name.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            BoobaseController.getControl().navigationTo(name);
        }
    }

    public void poiIndex(View view) {

    }


    public void stop(View view) {
        BoobaseController.getControl().stopMove();
    }

    public void startActivity(View view) {
        startActivity(new Intent(this, Main3Activity.class));
    }

    public void startCharge(View view) {
        BoobaseController.getControl().chargeStart();
    }

    public void stopCharge(View view) {
        BoobaseController.getControl().chargeCancel();
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void move(final MoveStatus moveStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(moveStatus.getMsg());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
