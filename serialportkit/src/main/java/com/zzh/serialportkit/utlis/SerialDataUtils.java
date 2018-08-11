package com.zzh.serialportkit.utlis;

/**
 * Created by zzh on 2018/7/25.
 * 串口数据转换工具类
 */

public class SerialDataUtils {
    //-------------------------------------------------------
    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    public static int isOdd(int num) {
        return num & 1;
    }

    //-------------------------------------------------------
    //字节数组异或运算
    public static byte byteArrXor(byte[] arr){
        byte temp =arr[0];
        for(int i=1;i<arr.length;i++){
            temp^=arr[i];
        }
        return temp;
    }


    //-------------------------------------------------------
    // int数值转换为占4个字节的(高位在前低位在后顺序)byte数组
    public static byte[] intToByteArr(int value) {
        byte[] b = new byte[4];
        b[0] = (byte) ((value >> 24) & 0xFF);
        b[1] = (byte) ((value >> 16) & 0xFF);
        b[2] = (byte) ((value >> 8) & 0xFF);
        b[3] = (byte) (value & 0xFF);
        return b;
    }


    //-------------------------------------------------------
    //  4个字节的byte数组中转int数值，本方法适用于(高位在前低位在后顺序)

    /**
     * @param bytes
     * @param offset 从数组的第offset位开始
     * @returnb
     */
    public static int bytesToInt2(byte[] bytes, int offset) {
        int value;
        value = (int) (((bytes[offset] & 0xFF) << 24)
                | ((bytes[offset + 1] & 0xFF) << 16)
                | ((bytes[offset + 2] & 0xFF) << 8)
                | (bytes[offset + 3] & 0xFF));
        return value;
    }

    //-------------------------------------------------------
    //Hex字符串转int
    public static int HexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }

    //-------------------------------------------------------
    //使用1字节就表示，表示需要2个16进行数
    public static String IntToHex(int b) {
        return String.format("%02x", b);//2表示需要两个16进行数
    }

    //需要使用2字节表示，表示需要4个16进行数
    public static String IntToHex2(int b) {
        return String.format("%04x", b);
    }

    //需要使用4字节表示，表示需要8个16进行数
    public static String IntToHex4(int b) {
        return String.format("%08x", b);
    }

    //-------------------------------------------------------
    //Hex字符串转byte
    public static byte HexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    //-------------------------------------------------------
    //1字节转2个Hex字符
    public static String Byte2Hex(Byte inByte) {
        return String.format("%02x", new Object[]{inByte}).toUpperCase();
    }

    //-------------------------------------------------------
    //字节数组转转hex字符串
    public static String ByteArrToHex(byte[] inBytArr) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte valueOf : inBytArr) {
            strBuilder.append(Byte2Hex(Byte.valueOf(valueOf)));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    //-------------------------------------------------------
    //字节数组转转hex字符串，可选长度
    public static String ByteArrToHex(byte[] inBytArr, int offset, int byteCount) {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(Byte2Hex(Byte.valueOf(inBytArr[i])));
        }
        return strBuilder.toString();
    }


    //-------------------------------------------------------
    //转hex字符串转字节数组
    public static byte[] HexToByteArr(String inHex) {
        byte[] result;
        int hexlen = inHex.length();
        if (isOdd(hexlen) == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }
}
