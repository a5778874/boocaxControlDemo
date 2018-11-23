package com.example.boobasedriver2.steer;


import com.iflytek.aiui.uartkit.util.SerialDataUtils;

import java.util.List;

public class SteerCMDConvertor {

    private static SteerCMDConvertor instance;

    private static final String WRITE_HEAD = "0101";

    private static final int standardRightShoulderValue = 357;    //右肩中值
    private static final int standardLeftShoulderValue = 670;     //左肩中值
    private static final int standardValue = 512;     //标准中值

    private SteerCMDConvertor() {
    }


    public static synchronized SteerCMDConvertor getInstance() {
        if (instance == null) {
            instance = new SteerCMDConvertor();
        }
        return instance;
    }

    //接收角度和控制位置,角度转为控制命令 ，0-1024十六进制表示，每+1 = +0.29度！
    private String angleToCmd(int angle, int whichBody) {
        int CenterValue = standardValue;      //中值
        switch (whichBody) {
            case SteerBody.ShoulderLeft:
                //默认左肩正度数为逆时针移动，与右肩相反
                //修改左肩与右肩一样为正数顺时针转，负数逆时针转
                angle = 0 - angle;
                CenterValue = standardLeftShoulderValue;
                break;
            case SteerBody.ShoulderRight:
                CenterValue = standardRightShoulderValue;
                break;
            default:
                CenterValue = standardValue;
                break;
        }

        int result = CenterValue + (int) (angle / 0.29);
        String cmd = SerialDataUtils.IntToHex2(result);
        return cmd;
    }

    //转为控制命令
    public String formatCmd(List<SteerBean> lists) {
        //  int len = steerBeans.length * 4;
        int len = lists.size() * 4;
        int crc = 1 + 1 + len;  //校验位，所有数据相加
        String dataLen = SerialDataUtils.IntToHex(len);  // 数据长度 = 头部占1字节 + Type占1字节+ 每个控制舵机命令占4字节 + 校验位1字节
        String datas = "";

        for (SteerBean steerBean : lists) {
            int body = steerBean.getControlBody(); //部位id
            String bodyCmd = SerialDataUtils.IntToHex(body);

            String angleCmd = angleToCmd(steerBean.getAngle(), body);

            //算出角度命令的2位数相加
            int subAngleCnd = 0;
            if (angleCmd.length() == 4) {
                subAngleCnd = SerialDataUtils.HexToInt(angleCmd.substring(0, 2)) + SerialDataUtils.HexToInt(angleCmd.substring(2, 4));
            }
            int speed = steerBean.getSpeed();
            String speedCmd = SerialDataUtils.IntToHex(speed);

            datas += (bodyCmd + angleCmd + speedCmd);

            crc += (body + subAngleCnd + speed);
        }


        String crcHex = SerialDataUtils.IntToHex(crc);

        crcHex = crcHex.substring(crcHex.length() - 2);//保留低2位

        StringBuffer cmd = new StringBuffer();
        cmd.append(WRITE_HEAD).append(dataLen).append(datas).append(crcHex);

        System.out.println(cmd.toString());
        return cmd.toString();
    }
}
