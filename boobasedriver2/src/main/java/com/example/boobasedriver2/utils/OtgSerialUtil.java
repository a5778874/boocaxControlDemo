package com.example.boobasedriver2.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.usb.UsbManager;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

/**
 * create by zzh on 2018/10/31
 */
public class OtgSerialUtil {
    private static final String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";
    private static OtgSerialUtil instance = null;
    private Context context;
    private CH34xUARTDriver driver;

    private OtgSerialUtil(Context context) {
        this.context = context;
    }

    public static synchronized OtgSerialUtil getInstance(Context context) {
        if (instance == null) {
            instance = new OtgSerialUtil(context);
        }
        return instance;
    }

    public void initUSBDriver() {
        driver = new CH34xUARTDriver(
                (UsbManager) context.getSystemService(Context.USB_SERVICE), context,
                ACTION_USB_PERMISSION);
        if (!driver.UsbFeatureSupported())// 判断系统是否支持USB HOST
        {
            Dialog dialog = new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("您的手机不支持USB HOST，请更换其他手机再试！")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    System.exit(0);
                                }
                            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

    }


}
