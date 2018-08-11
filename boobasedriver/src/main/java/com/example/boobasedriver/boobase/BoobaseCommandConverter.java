package com.example.boobasedriver.boobase;

import android.util.Log;

import com.example.boobasedriver.model.MyInteger;
import com.zzh.serialportkit.utlis.SerialDataUtils;

import java.io.UnsupportedEncodingException;


/**
 * create by zzh on 2018/7/27
 * <p>
 * 用于把一些控制命令参数转为协议中的格式
 * 内容： Head Len Cmd Payload Crc
 * 字节： 2 1 1 n 1
 * Head：协议头，固定两个字节： 0xAA 0x55
 * Len：数据长度，1 个字节，Cmd 和 Payload 的数据长度之和
 * functionCode：功能码，1 个字节
 * Payload：实际数据，长度 0-254 之间
 * Crc：校验码，一个字节。Len，Cmd，Payload 这三个部分的数据逐字节异或
 **/
public class BoobaseCommandConverter {
    private static String TAG = "TAG";

    private static final String HEAD = "AA55";


    private static BoobaseCommandConverter instance;


    private BoobaseCommandConverter() {
    }


    public static synchronized BoobaseCommandConverter getInstance() {
        if (instance == null) {
            instance = new BoobaseCommandConverter();
        }
        return instance;
    }


    /**
     * @param functionCode 功能码
     * @param payloads     控制参数
     * @return 生成的协议码
     */
    public String convertProtocols(String functionCode, Object... payloads) {

        //数据长度，每个payload占4个字节+ 1个字节的命令长度(payloads数组含字符串除外)，用1个字节的16进制字符串表示
        // int len=payloads.length * 4 + 1;
        int len = 1;


        //把Payload的几个参数转为协议中的16进制字符串表示，协议规定float类型的话先*1000转为整形，再转为4个字节的16进制字符串表示
        String payloadHexStr = "";
        for (Object payload : payloads) {
            if (payload instanceof MyInteger) {
                len += 1; //每个MyInteger占1个字节
                payloadHexStr += SerialDataUtils.IntToHex(((MyInteger) payload).getMyInteger());
            } else if (payload instanceof Float) {
                len += 4; //每个float占4个字节
                payloadHexStr += SerialDataUtils.IntToHex4((int) ((float) payload * 1000));
            } else if (payload instanceof Integer) {
                len += 4; //每个int占4个字节
                payloadHexStr += SerialDataUtils.IntToHex4((Integer) payload);
            } else if (payload instanceof String) {
                try {
                    //协议规定字符串使用utf-8编码
                    byte[] bytes = ((String) payload).getBytes("utf-8");
                    len += bytes.length; //每个字符串转为字节的长度
                    payloadHexStr += SerialDataUtils.ByteArrToHex(bytes).replace(" ", "");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        //数据长度
        String lenHexStr = SerialDataUtils.IntToHex(len);

        //校验码，Len，Cmd，Payload 这三个部分的数据逐字节异或，用1个字节的16进制字符串表示
        String crcHexStr = generateCrc(lenHexStr + functionCode + payloadHexStr);

        //拼接参数生成协议命令，每2个字符隔开
        //如向前用0.37m/s速度安全移动生成命令格式为： AA 55 0d 0A 00 00 01 72 00 00 00 00 00 00 00 00 74
        String cmd = formatCmd(HEAD + lenHexStr + functionCode + payloadHexStr + crcHexStr);

        Log.d(TAG, "生成的命令为: " + cmd);

        return cmd;
    }


    //生成校验码
    private String generateCrc(String hexStr) {
        return SerialDataUtils.Byte2Hex(SerialDataUtils.byteArrXor(SerialDataUtils.HexToByteArr(hexStr)));
    }


    //把命令中的字符串每两个字符用空格隔开
    private String formatCmd(String hexStr) {
        String regex = "(.{2})";
        String partition = hexStr.replaceAll(regex, "$1 ");
        return partition;
    }


}
