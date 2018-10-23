package com.example.aiui;

import android.content.Context;
import android.util.Log;

import com.example.aiui.processor.OTAProcessor;
import com.example.aiui.processor.SmartConfigProcessor;
import com.example.aiui.utils.Constant;
import com.example.aiui.utils.FileUtil;
import com.iflytek.aiui.uartkit.UARTAgent;
import com.iflytek.aiui.uartkit.constant.UARTConstant;
import com.iflytek.aiui.uartkit.entity.AIUIPacket;
import com.iflytek.aiui.uartkit.entity.CustomPacket;
import com.iflytek.aiui.uartkit.entity.MsgPacket;
import com.iflytek.aiui.uartkit.entity.WIFIConfPacket;
import com.iflytek.aiui.uartkit.listener.EventListener;
import com.iflytek.aiui.uartkit.listener.UARTEvent;
import com.iflytek.aiui.uartkit.util.PacketBuilder;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * create by zzh on 2018/8/31
 */
public class AiuiController {

    private final String TAG = "TAG";
    private UARTAgent agent;
    private static AiuiController instance;
    private Context context;
    private final static String GRAMMAR_FILE_PATH = "grammar/go.bnf";

    private AiuiController(Context context) {
        this.context = context;
        agent = UARTAgent.createAgent("/dev/ttyS0", 115200, new mEventListener());
    }

    public static synchronized AiuiController init(Context context) {
        if (instance == null) {
            instance = new AiuiController(context);
        }
        return instance;

    }

    public void startSpeak(String text) {
        if (agent != null)
            agent.sendMessage(PacketBuilder.obtainTTSStartPacket(text));
    }


    private MsgPacket recvPacket;

    //串口初始化事件监听
    private class mEventListener implements EventListener {
        @Override
        public void onEvent(final UARTEvent event) {
            switch (event.eventType) {
                case UARTConstant.EVENT_INIT_SUCCESS:
                    Log.d(TAG, ".....EVENT_INIT_SUCCESS...... ");
                    agent.sendMessage(PacketBuilder.obtainWIFIConfPacket(WIFIConfPacket.WIFIStatus.CONNECTED, WIFIConfPacket.EncryptMethod.WPA, "TP-LINK_Gmeri_ep", "guoji123"));

                    setSpeechConfig();
                    buildGrammar(context.getApplicationContext());
                    break;
                case UARTConstant.EVENT_INIT_FAILED:

                    break;
                case UARTConstant.EVENT_MSG:
                    //收到串口语音理解消息
                    recvPacket = (MsgPacket) event.data;
                    processPacket(recvPacket);
                    break;
                case UARTConstant.EVENT_SEND_FAILED:

                    break;

            }

        }
    }

    //判断是哪种消息包
    private void processPacket(MsgPacket packet) {
        switch (packet.getMsgType()) {
            case MsgPacket.AIUI_PACKET_TYPE:
                //AIUI相关消息包
                String content = new String(((AIUIPacket) packet).content);
                proecssAIUIPacket(content);
                Log.d(TAG, "接受到的aiui消息 : " + content);
                break;

            case MsgPacket.CUSTOM_PACKET_TYPE:

                Log.d(TAG, "接受到的自定义消息：" + Arrays.toString(((CustomPacket) packet).customData));
                break;
            case MsgPacket.CTR_PACKET_TYPE:
                Log.d(TAG, "CTR_PACKET_TYPE");
                break;
            default:
                break;
        }
    }

    //解析AIUI相关消息包
    private void proecssAIUIPacket(String contents) {
        try {
            JSONObject data = new JSONObject(contents);
            String type = data.optString("type", "");
            JSONObject content1 = data.optJSONObject("content");
            Log.d(TAG, "content1 " + content1.toString());

            //OTA message
            if (type.equals("ota")) {
                OTAProcessor.process(data);
            } else if (type.equals("smartConfig")) {
                SmartConfigProcessor.process(data);
                Log.d(TAG, "data " + data);
            } else if (type.equals("tts_event")) {


            } else if (type.equals("aiui_event")) {


                int eventType = content1.optInt("eventType");

                if (eventType == Constant.EVENT_RESULT) {
                    //AIUI Event
                    String sub = ((JSONObject) (content1.optJSONObject("info").optJSONArray("data").get(0))).optJSONObject("params").optString("sub");
                    Log.d(TAG, "sub: " + sub);
                    JSONObject intent = content1.optJSONObject("result").optJSONObject("intent");
                    if (sub.equals("nlp")) {
                        //aiui回答的消息
                        if (intent.has("answer")) {
                            JSONObject answer = intent.optJSONObject("answer");
                            final String myAsk = intent.optString("text");
                            final String AiuiRsp = answer.optString("text");

                            agent.sendMessage(PacketBuilder.obtainTTSStartPacket(AiuiRsp));

                            EventBus.getDefault().post(new RobotEvent(RobotMsg.EVENT_TYPE_NLP, new RobotMsg.NLPMsg(myAsk, AiuiRsp)));
                            Log.d(TAG, "AIUIController myAsk: " + myAsk + "  AiuiRsp: " + AiuiRsp);
                        }
                    } else if (sub.equals("asr")) {
                        //离线命令词回应
                        int rc = intent.optInt("rc");
                        Log.d(TAG, "asr rc: " + rc);
                        if (rc == 0) {
                            JSONObject asrData = (JSONObject) ((JSONObject) (intent.optJSONArray("ws").get(0))).optJSONArray("cw").get(0);

                            final int id = asrData.optInt("id");
                            final String question = asrData.optString("w");
                            EventBus.getDefault().post(new RobotEvent(RobotMsg.EVENT_TYPE_ASR, new RobotMsg.ASRMsg(id, question)));
                            Log.d(TAG, "AIUIController asr: " + question + "  id: " + id);
                        }

                    }
                } else if (eventType == Constant.EVENT_VAD) {
                    Log.d(TAG, "EVENT_VAD..........: " + content1.toString());
                } else if (eventType == Constant.EVENT_CMD_RETURN) {
                    Log.d(TAG, "......EVENT_CMD_RETURN..........: " + content1.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 开启离线命令配置
     */
    private void setAsrConfig() {

        try {
            JSONObject content = new JSONObject();
            content.put("threshold", 50);
            content.put("res_type", "assets");
            content.put("res_path", "asr/common.jet");


            JSONObject param = new JSONObject();
            param.put("asr", content);

            boolean issend = agent.sendMessage(PacketBuilder.obtainAIUICtrPacket(
                    AIUIConstant.CMD_SET_PARAMS,
                    0,
                    0,
                    param.toString(),
                    null));
            Log.d(TAG, "setAsrConfig: " + issend);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 设置语音识别为MIX
     */
    private void setSpeechConfig() {

        try {
            JSONObject content = new JSONObject();
            content.put("intent_engine_type", "mixed");
//            content.put("interact_mode", "continuous");
//            content.put("data_source", "sdk");
//            content.put("work_mode", "intent");


            JSONObject param = new JSONObject();
            param.put("speech", content);

            boolean issend = agent.sendMessage(PacketBuilder.obtainAIUICtrPacket(
                    AIUIConstant.CMD_SET_PARAMS,
                    0,
                    0,
                    param.toString(),
                    null));
            Log.d(TAG, "setAsrConfig: " + issend);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 建立离线命令的bnf语法
     */
    private void buildGrammar(Context context) {

        String grammar = FileUtil.readAssetsFile(context, GRAMMAR_FILE_PATH);
        Log.d(TAG, "buildGrammar: " + grammar);
        boolean issend = agent.sendMessage(PacketBuilder.obtainAIUICtrPacket(
                AIUIConstant.CMD_BUILD_GRAMMAR,
                0,
                0,
                grammar,
                null));
        Log.d(TAG, "setAsrConfig: " + issend);
    }


}
