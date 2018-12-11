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


import com.example.aiui.AiuiController;
import com.example.aiui.ControlCode;
import com.example.aiui.RobotEvent;
import com.example.aiui.RobotMsg;
import com.example.boobasedriver.boobase.BooBaseController;
import com.example.boobasedriver.boobase.BooBaseSetting;
import com.example.boobasedriver.eventbus.ChargeStatus;
import com.example.boobasedriver.eventbus.EmergencyStatus;
import com.example.boobasedriver.eventbus.LocationStatus;
import com.example.boobasedriver.eventbus.MoveStatus;
import com.example.boobasedriver.eventbus.SensorStatus;
import com.example.zzh.boocaxcontroldemo.utils.TimerTaskUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private EditText et_vx, et_vraw, et_x, et_y, et_raw, et_ssid, et_pwd;
    private TextView tv;
    private BooBaseController booBaseController;
    private BooBaseSetting booBaseSetting;
    private TimerTaskUtils timerTaskUtils;
    private AiuiController aiuiController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        booBaseController = BooBaseController.getInstance(getApplicationContext());
        booBaseSetting = BooBaseSetting.init(getApplicationContext());
        getLifecycle().addObserver(booBaseController);
        et_vx = findViewById(R.id.et_vx);
        et_vx.setText(booBaseSetting.getVelocityProperties() + "");
        et_vraw = findViewById(R.id.et_vraw);
        et_vraw.setText(booBaseSetting.getYawProperties() + "");
        et_x = findViewById(R.id.et_x);
        et_y = findViewById(R.id.et_y);
        et_raw = findViewById(R.id.et_raw);
        et_ssid = findViewById(R.id.et_ssid);
        et_pwd = findViewById(R.id.et_psd);
        tv = findViewById(R.id.tv);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());


        aiuiController = AiuiController.init(this);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRobotEvent(RobotEvent robotEvent) {
        switch (robotEvent.getMsg()) {

            case RobotMsg.EVENT_TYPE_ASR:

                RobotMsg.ASRMsg asrMsg = (RobotMsg.ASRMsg) robotEvent.getData();
                Log.d("TAG", "onRobotEvent EVENT_TYPE_ASR: " + asrMsg.getQuestion() + "...." + asrMsg.getAsrID());

                switch (asrMsg.getAsrID()) {
                    case ControlCode.BNF_MOVE_FORWARD:
                        booBaseController.moveForward();
                        break;
                    case ControlCode.BNF_MOVE_BACKWARD:
                        booBaseController.moveBackward();
                        break;
                    case ControlCode.BNF_TURN_LEFT:
                        booBaseController.turnRight();
                        break;
                    case ControlCode.BNF_TURN_RIGHT:
                        booBaseController.turnLeft();
                        break;
                    case ControlCode.BNF_FIRST_POI:
                        booBaseController.poiTo(0);
                        break;
                    case ControlCode.BNF_SECOND_POI:
                        booBaseController.poiTo(1);
                        break;
                    case ControlCode.BNF_THIRD_POI:
                        booBaseController.poiTo(2);
                        break;
                    case ControlCode.BNF_TRAVEL:
                        booBaseController.travelRandom(5f);
                        break;
                    case ControlCode.BNF_STOP:
                        booBaseController.stopMove();
                        booBaseController.travelStop();
                        break;
                }
                break;
            case RobotMsg.EVENT_TYPE_NLP:
                RobotMsg.NLPMsg nlpMsg = (RobotMsg.NLPMsg) robotEvent.getData();
                Log.d("TAG", "onRobotEvent EVENT_TYPE_NLP: " + nlpMsg.getQuestion() + ".." + nlpMsg.getAnswer());
                break;


        }
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
        Log.d("TAG", "onEmergencyStatusReciver: "+status.getEmergencyStatus());
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

    //传感器状态，0正常，非0异常
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSensorStatusReciver(SensorStatus status) {
        int uwbStatus = status.getUwbStatus();  //UWB状态
        int laserStatus = status.getLaserStatus();//激光状态
        int encodingDiskStatus = status.getEncodingDiskStatus();//码盘状态
        int boobaseStatus = status.getBoobaseStatus(); //底盘状态
        int cameraStatus = status.getCameraStatus();    //摄像头状态
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
                 aiuiController.startSpeak("静止待命中");
                tv.append("静止待命中（开机待命状态）\n");
                break;
            case 1:
                aiuiController.startSpeak("上次目标失败，等待新的导航命令");
                tv.append("(导航结束状态）上次目标失败，等待新的导航命令\n");
                break;
            case 2:
                aiuiController.startSpeak("上次目标完成");
                tv.append("(导航结束状态）上次目标完成，等待断的导航命令；\n");
                break;
            case 3:
                tv.append("（导航中状态）移动中，正在前往目的地\n");
                break;
            case 4:
                aiuiController.startSpeak("前方障碍物");
                tv.append("(导航中状态）前方障碍物；\n");
                break;
            case 5:
                aiuiController.startSpeak("目的地被遮档");
                tv.append("（导航中状态）目的地被遮档；\n");
                break;
            case 6:
                aiuiController.startSpeak("导航取消");
                tv.append("(导航结束状态) 导航取消 (用户主动取消）；\n");
                break;
            case 7:
                aiuiController.startSpeak("收到新目标点");
                tv.append("新目标点;每次收到导航目标点都会返回此状态，然后再切换到其他状态；\n");
                break;
            case 8:
                aiuiController.startSpeak("导航路径阻塞");
                aiuiController.startSpeak("上次目标失败，等待新的导航命令");
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
        aiuiController.startSpeak("向前走");
        booBaseController.moveForward();
    }

    public void moveBack(View view) {
        booBaseController.moveBackward();
        aiuiController.startSpeak("move back");
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

    public void connectWIFI(View view) {
        String ssid = et_ssid.getText().toString();
        String pwd = et_pwd.getText().toString();

        booBaseController.configureWIFI(ssid, pwd);

    }


}
