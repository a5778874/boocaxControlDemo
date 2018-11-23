package com.example.zzh.boocaxcontroldemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.boobasedriver2.boobase.BoobaseControllor;
import com.example.boobasedriver2.steer.SteerBean;
import com.example.boobasedriver2.steer.SteerBody;
import com.example.boobasedriver2.steer.SteerControllor;

/**
 * create by zzh on 2018/10/22
 */
public class Main3Activity extends BaseActivity {
    private EditText et_singleAngle, et_singleSpeed, et_singleBody;
    private EditText et_leftgroupShoulderAngle, et_leftgroupShoulderSpeed, et_leftgroupArmAngle, et_leftgroupArmSpeed, et_leftgroupElbowAngle, et_leftgroupElbowSpeed;
    private EditText et_rightgroupShoulderAngle, et_rightgroupShoulderSpeed, et_rightgroupArmAngle, et_rightgroupArmSpeed, et_rightgroupElbowAngle, et_rightgroupElbowSpeed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_3);
        initView();

    }

    private void initView() {
        et_singleAngle = findViewById(R.id.et_single_angle);
        et_singleSpeed = findViewById(R.id.et_single_speed);
        et_singleBody = findViewById(R.id.et_single_body);

        et_leftgroupShoulderAngle = findViewById(R.id.et_leftgroup1_angle);
        et_leftgroupShoulderSpeed = findViewById(R.id.et_leftgroup1_speed);
        et_leftgroupArmAngle = findViewById(R.id.et_leftgroup2_angle);
        et_leftgroupArmSpeed = findViewById(R.id.et_leftgroup2_speed);
        et_leftgroupElbowAngle = findViewById(R.id.et_leftgroup3_angle);
        et_leftgroupElbowSpeed = findViewById(R.id.et_leftgroup3_speed);

        et_rightgroupShoulderAngle = findViewById(R.id.et_rightgroup1_angle);
        et_rightgroupShoulderSpeed = findViewById(R.id.et_rightgroup1_speed);
        et_rightgroupArmAngle = findViewById(R.id.et_rightgroup2_angle);
        et_rightgroupArmSpeed = findViewById(R.id.et_rightgroup2_speed);
        et_rightgroupElbowAngle = findViewById(R.id.et_rightgroup3_angle);
        et_rightgroupElbowSpeed = findViewById(R.id.et_rightgroup3_speed);

    }

    public void startSingleAction(View view) {
        if (TextUtils.isEmpty(et_singleAngle.getText().toString())||TextUtils.isEmpty(et_singleAngle.getText().toString())||TextUtils.isEmpty(et_singleAngle.getText().toString()))return;
        int angle = Integer.parseInt(et_singleAngle.getText().toString());
        int speed = Integer.parseInt(et_singleSpeed.getText().toString());
        int body = Integer.parseInt(et_singleBody.getText().toString());
        SteerControllor.getInstance().sendAction(angle, speed, body);
    }


    public void startActionGroup(View view) {
        int leftshoulderAngle = Integer.parseInt(et_leftgroupShoulderAngle.getText().toString());
        int leftshoulderSpeed = Integer.parseInt(et_leftgroupShoulderSpeed.getText().toString());
        int leftarmAngle = Integer.parseInt(et_leftgroupArmAngle.getText().toString());
        int leftarmSpeed = Integer.parseInt(et_leftgroupArmSpeed.getText().toString());
        int leftelbowAngle = Integer.parseInt(et_leftgroupElbowAngle.getText().toString());
        int leftelbowSpeed = Integer.parseInt(et_leftgroupElbowSpeed.getText().toString());

        int rightshoulderAngle = Integer.parseInt(et_rightgroupShoulderAngle.getText().toString());
        int rightshoulderSpeed = Integer.parseInt(et_rightgroupShoulderSpeed.getText().toString());
        int rightarmAngle = Integer.parseInt(et_rightgroupArmAngle.getText().toString());
        int rightarmSpeed = Integer.parseInt(et_rightgroupArmSpeed.getText().toString());
        int rightelbowAngle = Integer.parseInt(et_rightgroupElbowAngle.getText().toString());
        int rightelbowSpeed = Integer.parseInt(et_rightgroupElbowSpeed.getText().toString());

        SteerControllor.getInstance().sendActionGroup(
                new SteerBean(leftshoulderAngle, leftshoulderSpeed, SteerBody.ShoulderLeft),
                new SteerBean(leftarmAngle, leftarmSpeed, SteerBody.ArmLeft),
                new SteerBean(leftelbowAngle, leftelbowSpeed, SteerBody.ElbowLeft),
                new SteerBean(rightshoulderAngle, rightshoulderSpeed, SteerBody.ShoulderRight),
                new SteerBean(rightarmAngle, rightarmSpeed, SteerBody.ArmRight),
                new SteerBean(rightelbowAngle, rightelbowSpeed, SteerBody.ElbowRight));
    }

    public void reset(View view) {
        SteerControllor.getInstance().resetBody();
    }

    public void bye(View view) {
        SteerControllor.getInstance().goodbye();
    }

    public void lefthand(View view) {
        SteerControllor.getInstance().handUpLeft();
    }

    public void righthand(View view) {
        SteerControllor.getInstance().handUpRight();
    }
}
