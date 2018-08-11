package com.example.zzh.boocaxcontroldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.boobasedriver.boobase.BooBaseController;
import com.example.boobasedriver.boobase.BooBaseSetting;
import com.example.boobasedriver.eventbus.ChargeStatus;
import com.example.boobasedriver.eventbus.EmergencyStatus;
import com.example.boobasedriver.eventbus.LocationStatus;
import com.example.boobasedriver.eventbus.MoveStatus;
import com.example.zzh.boocaxcontroldemo.utils.TimerTaskUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private EditText et_vx, et_vraw, et_x, et_y, et_raw;
    private TextView tv;
    private Button bt_safemove;
    private BooBaseController booBaseController;
    private BooBaseSetting booBaseSetting;
    private TimerTaskUtils timerTaskUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        booBaseController = BooBaseController.getInstance(getApplicationContext());
        booBaseSetting = BooBaseSetting.init(getApplicationContext());
        getLifecycle().addObserver(booBaseController);
        EventBus.getDefault().register(this);
        et_vx = findViewById(R.id.et_vx);
        et_vx.setText(booBaseSetting.getVelocityProperties() + "");
        et_vraw = findViewById(R.id.et_vraw);
        et_vraw.setText(booBaseSetting.getYawProperties() + "");
        et_x = findViewById(R.id.et_x);
        et_y = findViewById(R.id.et_y);
        et_raw = findViewById(R.id.et_raw);
        tv = findViewById(R.id.tv);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        bt_safemove = findViewById(R.id.safeMove);


        bt_safemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setTimer();
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChargeStatusReciver(ChargeStatus status) {
        switch (status.getChargeStatus()) {
            case 0:
                tv.append("未充电状态：正常运行\n");
                break;
            case 1:
                tv.append("充电状态：正在连接底座充电\n");
                break;
            case 2:
                tv.append("充电状态：正在连接线缆充电\n");
                break;
            case 3:
                tv.append("充电对接过程中：正在和底座对接\n");
                break;
            case 4:
                tv.append("充电脱离过程中：正在脱离充电座\n");
                break;
            case 5:
                tv.append("充电状态：已充满，但仍连接在充电座上\n");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEmergencyStatusReciver(EmergencyStatus status) {
        switch (status.getEmergencyStatus()) {
            case 0:
                tv.append("紧急开关开机后状态一直抬起未动过\n");
                break;
            case 1:
                tv.append("紧急开关抬起状态\n");
                break;
            case 2:
                tv.append("紧急开关按下状态\n");
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationStatusReciver(LocationStatus status) {
        switch (status.getLocationStatus()) {
            case 0:
                tv.append("定位正常\n");
                break;
            case 1:
                tv.append("定位准备/正在尝试定位\n");
                break;
            case 2:
                tv.append("(异常状态）还没构建地图，无法定位\n");
                break;
            case 3:
                tv.append("(构图状态）正在构建地图中\n");
                break;
            case 4:
                tv.append("(异常状态）UWB错误\n");
                break;
            case 5:
                tv.append("（构图状态）正在进行回环检测，优化地图\n");
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoveStatusReciver(MoveStatus status) {
        switch (status.getMoveStatus()) {
            case 0:
                tv.append("静止待命中（开机待命状态）\n");
                break;
            case 1:
                tv.append("(导航结束状态）上次目标失败，等待新的导航命令\n");
                break;
            case 2:
                tv.append("(导航结束状态）上次目标完成，等待断的导航命令；\n");
                break;
            case 3:
                tv.append("（导航中状态）移动中，正在前往目的地\n");
                break;
            case 4:
                tv.append("(导航中状态）前方障碍物；\n");
                break;
            case 5:
                tv.append("（导航中状态）目的地被遮档；\n");
                break;
            case 6:
                tv.append("(导航结束状态) 导航取消 (用户主动取消）；\n");
                break;
            case 7:
                tv.append("新目标点;每次收到导航目标点都会返回此状态，然后再切换到其他状态；\n");
                break;
            case 8:
                tv.append("（导航中状态）导航路径阻塞；\n");
                break;
        }

    }

    private void setTimer() {
        timerTaskUtils = new TimerTaskUtils(100, new TimerTask() {
            @Override
            public void run() {
                move();

            }
        });
        timerTaskUtils.start();
    }


    public void move() {
        Log.d("TAG", "move..... ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (timerTaskUtils != null) {
            timerTaskUtils.stop();
            timerTaskUtils = null;
        }

    }

    public void stop(View view) {

        if (timerTaskUtils != null) {
            timerTaskUtils.stop();
            timerTaskUtils = null;
        }
        booBaseController.stopMove();
        Log.d("TAG", "move stop.... ");
    }

    public void moveFront(View view) {
        booBaseController.moveForward();
    }

    public void moveBack(View view) {
        booBaseController.moveBackward();
    }

    public void moveLeft(View view) {
        booBaseController.turnLeft();
    }

    public void moveRight(View view) {
        booBaseController.turnRight();
    }

    public void savep(View view) {
        booBaseSetting.setYawProperties(Float.parseFloat(et_vraw.getText().toString()));
        booBaseSetting.setVelocityProperties(Float.parseFloat(et_vx.getText().toString()));
        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
    }

    public void naviTo(View view) {
        float x = Float.parseFloat(et_x.getText().toString());
        float y = Float.parseFloat(et_y.getText().toString());
        float raw = Float.parseFloat(et_raw.getText().toString());
        booBaseController.navigationTo(x, y, raw);
    }


    public void travelOridinal(View view) {
        booBaseController.travelOrdinal(5.0f, false);
    }

    public void travelRandom(View view) {
        booBaseController.travelRandom(1000f);
    }

    public void travelCancel(View view) {
        booBaseController.travelStop();
    }

    public void travelContinue(View view) {
        booBaseController.travelContinue();
    }

    public void chargeStart(View view) {
        booBaseController.chargeStart();
    }

    public void chargeStop(View view) {
        booBaseController.chargeCancel();
    }


    public void clear(View view) {
        tv.setText("");
    }

    public void poiByName(View view) {
        booBaseController.poiTo("点位一");
    }

    public void poiByIndex(View view) {
        booBaseController.poiTo(0);
    }
}
