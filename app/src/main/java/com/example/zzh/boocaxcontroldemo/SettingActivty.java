package com.example.zzh.boocaxcontroldemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.boobasedriver2.utils.SharePreferenceUtils;
import com.iflytek.aiui.uartkit.util.SerialPortUtil;

import java.util.Map;


/**
 * create by zzh on 2018/11/23
 */
public class SettingActivty extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup rg_boobase, rg_steer, rg_aiui;
    private RadioButton boobaseCom0, boobaseCom1, boobaseCom2, boobaseCom3, boobaseCom4;
    private RadioButton steerCom0, steerCom1, steerCom2, steerCom3, steerCom4;
    private RadioButton aiuiCom0, aiuiCom1, aiuiCom2, aiuiCom3, aiuiCom4;
    private EditText et_boobaseRate, et_aiuiRate, et_steerRate;

    private Map<String, RadioButton> boobaseComMap = new ArrayMap<>();
    private Map<String, RadioButton> aiuiComMap = new ArrayMap<>();
    private Map<String, RadioButton> steerComMap = new ArrayMap<>();

    public static final String COM0 = "/dev/ttyS0";
    public static final String COM1 = "/dev/ttyS1";
    public static final String COM2 = "/dev/ttyS2";
    public static final String COM3 = "/dev/ttyS3";
    public static final String COM4 = "/dev/ttyS4";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
        getConfig();
    }

    //绑定单选按钮值
    private void initData() {
        boobaseComMap.put(COM0, boobaseCom0);
        boobaseComMap.put(COM1, boobaseCom1);
        boobaseComMap.put(COM2, boobaseCom2);
        boobaseComMap.put(COM3, boobaseCom3);
        boobaseComMap.put(COM4, boobaseCom4);
        aiuiComMap.put(COM0, aiuiCom0);
        aiuiComMap.put(COM1, aiuiCom1);
        aiuiComMap.put(COM2, aiuiCom2);
        aiuiComMap.put(COM3, aiuiCom3);
        aiuiComMap.put(COM4, aiuiCom4);
        steerComMap.put(COM0, steerCom0);
        steerComMap.put(COM1, steerCom1);
        steerComMap.put(COM2, steerCom2);
        steerComMap.put(COM3, steerCom3);
        steerComMap.put(COM4, steerCom4);
    }

    private void getConfig() {
        String boobaseCom = (String) SharePreferenceUtils.getParam(getApplicationContext(), "boobase", "");
        String steer = (String) SharePreferenceUtils.getParam(getApplicationContext(), "steer", "");
        String aiui = (String) SharePreferenceUtils.getParam(getApplicationContext(), "aiui", "");
        String boobaseRate = (String) SharePreferenceUtils.getParam(getApplicationContext(), "boobaseRate", "");
        String steerRate = (String) SharePreferenceUtils.getParam(getApplicationContext(), "steerRate", "");
        String aiuiRate = (String) SharePreferenceUtils.getParam(getApplicationContext(), "aiuiRate", "");
        if (!TextUtils.isEmpty(boobaseCom)) {
            boobaseComMap.get(boobaseCom).setChecked(true);
        }
        if (!TextUtils.isEmpty(steer)) {
            steerComMap.get(steer).setChecked(true);
        }
        if (!TextUtils.isEmpty(aiui)) {
            aiuiComMap.get(aiui).setChecked(true);
        }
        if (!TextUtils.isEmpty(boobaseRate)) {
            et_boobaseRate.setText(boobaseRate);
        }
        if (!TextUtils.isEmpty(steerRate)) {
            et_steerRate.setText(steerRate);
        }
        if (!TextUtils.isEmpty(aiuiRate)) {
            et_aiuiRate.setText(aiuiRate);
        }
    }

    private void initView() {
        et_aiuiRate = findViewById(R.id.et_aiuirate);
        et_boobaseRate = findViewById(R.id.et_boobaserate);
        et_steerRate = findViewById(R.id.et_steerrate);
        rg_boobase = findViewById(R.id.rg_boobase);
        rg_boobase.setOnCheckedChangeListener(this);
        rg_steer = findViewById(R.id.rg_steer);
        rg_steer.setOnCheckedChangeListener(this);
        rg_aiui = findViewById(R.id.rg_aiui);
        rg_aiui.setOnCheckedChangeListener(this);
        boobaseCom0 = findViewById(R.id.boobase_com0);
        boobaseCom1 = findViewById(R.id.boobase_com1);
        boobaseCom2 = findViewById(R.id.boobase_com2);
        boobaseCom3 = findViewById(R.id.boobase_com3);
        boobaseCom4 = findViewById(R.id.boobase_com4);
        steerCom0 = findViewById(R.id.steer_com0);
        steerCom1 = findViewById(R.id.steer_com1);
        steerCom2 = findViewById(R.id.steer_com2);
        steerCom3 = findViewById(R.id.steer_com3);
        steerCom4 = findViewById(R.id.steer_com4);
        aiuiCom0 = findViewById(R.id.aiui_com0);
        aiuiCom1 = findViewById(R.id.aiui_com1);
        aiuiCom2 = findViewById(R.id.aiui_com2);
        aiuiCom3 = findViewById(R.id.aiui_com3);
        aiuiCom4 = findViewById(R.id.aiui_com4);
    }

    //保存配置
    public void save(View view) {
        if (!TextUtils.isEmpty(boobaseCom))
            SharePreferenceUtils.setParam(getApplicationContext(), "boobase", boobaseCom);
        if (!TextUtils.isEmpty(steerCom))
            SharePreferenceUtils.setParam(getApplicationContext(), "steer", steerCom);
        if (!TextUtils.isEmpty(aiuiCom))
            SharePreferenceUtils.setParam(getApplicationContext(), "aiui", aiuiCom);
        if (!TextUtils.isEmpty(et_boobaseRate.getText().toString()))
            SharePreferenceUtils.setParam(getApplicationContext(), "boobaseRate", et_boobaseRate.getText().toString());
        if (!TextUtils.isEmpty(et_steerRate.getText().toString()))
            SharePreferenceUtils.setParam(getApplicationContext(), "steerRate", et_steerRate.getText().toString());
        if (!TextUtils.isEmpty(et_aiuiRate.getText().toString()))
            SharePreferenceUtils.setParam(getApplicationContext(), "aiuiRate", et_aiuiRate.getText().toString());
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    private String boobaseCom;
    private String steerCom;
    private String aiuiCom;

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.boobase_com0:
                boobaseCom = COM0;
                break;
            case R.id.boobase_com1:
                boobaseCom = COM1;
                break;
            case R.id.boobase_com2:
                boobaseCom = COM2;
                break;
            case R.id.boobase_com3:
                boobaseCom = COM3;
                break;
            case R.id.boobase_com4:
                boobaseCom = COM4;
                break;
            case R.id.steer_com0:
                steerCom = COM0;
                break;
            case R.id.steer_com1:
                steerCom = COM1;
                break;
            case R.id.steer_com2:
                steerCom = COM2;
                break;
            case R.id.steer_com3:
                steerCom = COM3;
                break;
            case R.id.steer_com4:
                steerCom = COM4;
                break;
            case R.id.aiui_com0:
                aiuiCom = COM0;
                break;
            case R.id.aiui_com1:
                aiuiCom = COM1;
                break;
            case R.id.aiui_com2:
                aiuiCom = COM2;
                break;
            case R.id.aiui_com3:
                aiuiCom = COM3;
                break;
            case R.id.aiui_com4:
                aiuiCom = COM4;
                break;

        }
    }
}
