package com.example.zzh.boocaxcontroldemo.aiui;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.boobasedriver2.RobotManager;
import com.example.boobasedriver2.boobase.BoobaseControllor;
import com.example.zzh.boocaxcontroldemo.Bean.RobotStatus;
import com.example.zzh.boocaxcontroldemo.Myapplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * create by zzh on 2018/10/25
 */
public class SpeakHandler implements SpeakListener {

    private Timer timer;
    private TimerTask task;
    public static boolean isTimer = false;//是否开启Timer
    public static long speakOverTime; //当前说话完成的时间
    public static long wakeupTime = 30 * 1000; //唤醒时间
    public static long backTime = 60 * 1000;  //回到起始位置时间
    private String TAG = "TAG";

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (RobotStatus.getInstance().isSpeaking()) {
                    cancelTimer();
                    return;
                }

                long waitTime = System.currentTimeMillis();

                //到达单点导航的目的地
                if (RobotStatus.getInstance().isCurrentNavEnd()) {
                    if ((waitTime - speakOverTime) > backTime) {
                        Log.d(TAG, "---回起始位置-----");
                        String station = Myapplication.getInstance().station;
                        if (!TextUtils.isEmpty(station)) {
                            RobotManager.getControl().navigationTo(station); //一段时间无交互返回岗位
                        }
                        //todo 导航完成开始人脸识别
                        //重置导航和参观状态
                        RobotStatus.getInstance().setCurrentNavEnd(false);
                        RobotStatus.getInstance().setNextVisitPoint("");
                        RobotStatus.getInstance().setNextVisitAnswer("");
                    }
                }


            }
        }
    };


    @Override
    public void onSpeakBegin(String text) {
        cancelTimer();
        RobotStatus.getInstance().setBeginSpeakTime(System.currentTimeMillis()); // 设置开始说话时间
        RobotStatus.getInstance().setSpeaking(true);
    }

    @Override
    public void onSpeakOver(int error) {
        resetSpeak();
        speakOverTime = System.currentTimeMillis();
        if (!RobotStatus.getInstance().isCurrentVisit()) {
            //非参观模式
            if (RobotStatus.getInstance().isCurrentNavEnd()) {
                if (!isTimer) {
                    getIsNextNav();
                }
            }
        } else {
            //唤醒了参观模式下
            if (RobotStatus.getInstance().isCurrentVisitWakeup()) {
                if (!isTimer) {
                    getIsNextNav();
                }
            }
        }

    }


    //重置说话状态
    public void resetSpeak() {
        RobotStatus.getInstance().setEndSpeakTime(System.currentTimeMillis()); // 设置结束说话时间
        RobotStatus.getInstance().setSpeaking(false);
    }


    //每隔一段时间发送一个消息
    public void getIsNextNav() {
        Log.d(TAG, "getIsNextNav----");
        cancelTimer();
        isTimer = true;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 0, 5000);
    }


    public void cancelTimer() {
        isTimer = false;
        if (timer != null) {
            timer.cancel();
        }
    }
}
