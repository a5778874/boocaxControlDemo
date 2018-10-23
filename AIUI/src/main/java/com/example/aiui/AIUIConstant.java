package com.example.aiui;

 class AIUIConstant {



    //==============AIUIMessage=====================

    /**
     *
     * 发送json信息
     *
     {
     "type": "aiui_msg",
     "content": {
     "msg_type": 8, //CMD_RESET_WAKEUP  重置AIUI唤醒状态
     "arg1": 0,
     "arg2": 0,
     "params": "",
     "data": "" //非必须，值为原数据的base64编码
     }
     }

     有返回的含义是在向 AIUI 发送一条 CMD消息后，AIUI会抛出一个对应的EVENT_CMD_RETURN事件返回CMD消息的处理结果。
     */

    //获取交互状态
    public final static int CMD_GET_STATE = 1;
    //向AIUI服务器写入数据
    public final static int CMD_WRITE = 2;
    //停止写入数据
    public final static int CMD_STOP_WRITE = 3;
    //重置AIUI服务的状态
    public final static int CMD_RESET = 4;
    //启动AIUI服务
    public final static int CMD_START = 5;
    //停止AIUI服务
    public final static int CMD_STOP  = 6;
    //唤醒消息
    public final static int CMD_WAKEUP = 7;
    //重置唤醒状态
    public final static int CMD_RESET_WAKEUP = 8;
    //设置麦克风阵列的拾音波束
    public final static int CMD_SET_BEAM = 9;
    //设置参数配置
    public final static int CMD_SET_PARAMS = 10;
    //发送应用日志到云端，可以帮助分析应用问题
    public final static int CMD_SEND_LOG = 12;
    //同步个性化数据
    public final static int CMD_SYNC = 13;
    //构建本地语法
    public final static int CMD_BUILD_GRAMMAR = 16;
    //更新本地词表
    public final static int CMD_UPDATE_LOCAL_LEXICON = 17;
    //开始抛出识别音频
    public final static int CMD_START_THROW_AUDIO = 18;
    //停止抛出识别音频
    public final static int CMD_STOP_THROW_AUDIO = 19;
    //结果确认
    public final static int CMD_RESULT_VALIDATION_ACK = 20;
    //清空交互历史
    public final static int CMD_CLEAN_DIALOG_HISTORY = 21;
    //开始录制数据
    public final static int CMD_START_RECORD = 22;
    //停止录制数据
    public final static int CMD_STOP_RECORD = 23;
    //查询数据同步状态
    public final static int CMD_QUERY_SYNC_STATUS = 24;





    //==================AIUIEvent===================

    //结果事件
    public final static int EVENT_RESULT = 1;
    //出错事件
    public final static int EVENT_ERROR = 2;
    //服务状态事件
    public final static int EVENT_STATE = 3;
    //唤醒事件
    public final static int EVENT_WAKEUP = 4;
    //休眠事件  (唤醒进入等待)
    public final static int EVENT_SLEEP  = 5;
    //VAD事件  (唤醒中等待)
    public final static int EVENT_VAD = 6;
    //某条CMD命令对应的返回事件
    public final static int EVENT_CMD_RETURN = 8;
    //音频抛出事件
    public final static int EVENT_AUDIO = 9;
    //准备休眠事件
    public final static int EVENT_PRE_SLEEP = 10;
    //抛出该事件通知外部录音开始，用户可以开始说话
    public final static int EVENT_START_PECORD = 11;
    //通知外部录音停止
    public final static int EVENT_STOP_PECOED = 12;
    //与服务端建立起连接事件
    public final static int EVENT_CONNECTED_TO_SERVER = 13;
    //与服务端断开连接事件
    public final static int EVENT_SERVER_DISCONNECTED = 14;



    //==============SYNC_DATA_SCHEMA==============

    //同步动态实体
    public final static int SYNC_DATA_SCHEMA = 3;


}
