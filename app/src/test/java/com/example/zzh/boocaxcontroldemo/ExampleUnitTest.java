package com.example.zzh.boocaxcontroldemo;

import android.os.SystemClock;

import com.example.boobasedriver2.boobase.BoobaseController;
import com.example.zzh.boocaxcontroldemo.Bean.WorkingStatus;
import com.example.zzh.boocaxcontroldemo.proto.myTest;
import com.google.protobuf.InvalidProtocolBufferException;
import com.iflytek.aiui.uartkit.util.SerialDataUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
     public static WorkingStatus workingStatus;
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

    }

    @Test
    public void Test(){


    }

    @Test
    public void Test2(){


    }


    @Test
    public void test(){

        //编码编成字节数组
        myTest.Person.Builder builder = myTest.Person.newBuilder();
        builder.setId(101);
        builder.setName("ashi你好");
        byte[] bytes = builder.build().toByteArray();  //转为字节数组
        String byteStr=SerialDataUtils.ByteArrToHex(bytes);  //16进制显示: 08 65 12 0A 61 73 68 69 E4 BD A0 E5 A5 BD
        System.out.println(byteStr);

        //解编码
        myTest.Person.Builder builder1 = myTest.Person.newBuilder();
        myTest.Person person=builder1.build();
        try {
            person= myTest.Person.parseFrom(SerialDataUtils.HexToByteArr(byteStr.trim().replace(" ","")));
            System.out.println(person.getId()+".."+person.getName());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

    }

}