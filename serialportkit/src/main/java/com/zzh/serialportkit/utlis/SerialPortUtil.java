package com.zzh.serialportkit.utlis;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

/**
 * Created by zzh on 2018/7/25.
 */

public class SerialPortUtil {
    private String TAG = SerialPortUtil.class.getSimpleName();
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    //private int baudrate = 9600;//波特率
    private static SerialPortUtil portUtil;
    private OnDataReceiveListener onDataReceiveListener = null;
    private boolean isStop = false;


    private SerialPortUtil(){

    }

    public interface OnDataReceiveListener {
        public void onDataReceive(byte[] buffer, int size);
    }

    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }

    public static SerialPortUtil getInstance(String path,int baudrate) {
        if (null == portUtil) {
            portUtil=new SerialPortUtil();
        }
        portUtil.onCreate(path,baudrate);
        return portUtil;
    }


    /**
     * 初始化串口信息
     */
    private void onCreate(String path,int baudrate) {
        try {
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

            mReadThread = new ReadThread();
            isStop = false;
            mReadThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送指令到串口
     *
     * @param cmd
     * @return
     */
    public boolean sendCmds(String cmd) {
        boolean result = true;
        String str = cmd;
        str = str.replace(" ", "");
        byte[] mBuffer = SerialDataUtils.HexToByteArr(str);
        if (!isStop) {
            try {
                if (mOutputStream != null) {
                    mOutputStream.write(mBuffer);
                } else {
                    result = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
        } else {
            System.out.println("sendCmds serialPort isClose");
            result = false;
        }

        return result;
    }


    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            System.out.println("ReadThread.run isInterrupted()=" + isInterrupted());
            while (!isStop && !isInterrupted()) {
                System.out.println("ReadThread.run mInputStream=" + mInputStream);
                int size;
                try {
                    if (mInputStream == null){
                        System.out.println("ReadThread.run return");
                        return;
                    }

                    byte[] buffer = new byte[512];
                    System.out.println("ReadThread.run buffer");
                    size = mInputStream.read(buffer);//该方法读不到数据时，会阻塞在这里
                    System.out.println("ReadThread.run size=" + size);
                    if (size > 0) {
                       /* if(MyLog.isDyeLevel()){
                            MyLog.log(TAG, MyLog.DYE_LOG_LEVEL, "length is:"+size+",data is:"+new String(buffer, 0, size));
                        }*/
                        byte[] buffer2 = new byte[size];
                        for (int i = 0; i < size; i++) {
                            buffer2[i] = buffer[i];
                        }
                        if (onDataReceiveListener != null) {
                            onDataReceiveListener.onDataReceive(buffer2, size);
                        }
                    }
                    //Thread.sleep(50);//延时 50 毫秒
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ReadThread.run  e.printStackTrace() " + e);
                    return;
                }
            }
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        isStop = true;
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        if (mSerialPort != null) {
            mSerialPort.close();
        }
    }
}
