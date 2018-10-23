package com.example.zzh.boocaxcontroldemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.boobasedriver2.boobase.BoobaseController;

/**
 * create by zzh on 2018/10/22
 */
public class Main3Activity  extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    public void moveForward(View view) {
        BoobaseController.getControl().moveForward();
    }

    public void moveBackward(View view) {
            BoobaseController.getControl().moveBackward();
    }
}
