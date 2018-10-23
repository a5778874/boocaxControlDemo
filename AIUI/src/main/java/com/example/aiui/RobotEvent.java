package com.example.aiui;

import android.support.annotation.Nullable;

/**
 * create by zzh on 2018/8/31
 */
public class RobotEvent {


        private int msg;
        private Object data;

        public RobotEvent(int msg, @Nullable Object data){
            this.msg = msg;
            this.data= data;
        }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
