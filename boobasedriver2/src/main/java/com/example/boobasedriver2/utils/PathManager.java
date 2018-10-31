package com.example.boobasedriver2.utils;

import android.util.Log;

/**
 * create by zzh on 2018/10/29
 */


public class PathManager {
    public static String CONFIGURATION_PATH = "/gjdl/cfg/";   //配置文件路径

    //安装时生成需要的文件夹
    public static void initPath() {
        FileUtil.PathStatus status=FileUtil.createSDPath(CONFIGURATION_PATH);
        Log.d("TAG", "初始化文件夹: "+status);
    }



}
