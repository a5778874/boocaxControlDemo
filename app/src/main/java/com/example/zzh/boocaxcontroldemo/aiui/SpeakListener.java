package com.example.zzh.boocaxcontroldemo.aiui;

/**
 * create by zzh on 2018/10/25
 */
public interface SpeakListener {
    void onSpeakBegin(String text);

    void onSpeakOver(int error);
}
