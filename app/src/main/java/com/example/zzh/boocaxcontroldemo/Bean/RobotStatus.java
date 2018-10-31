package com.example.zzh.boocaxcontroldemo.Bean;

/**
 * create by zzh on 2018/10/25
 */
public class RobotStatus {

    private static RobotStatus instance = null;

    private RobotStatus() {
    }

    synchronized public static RobotStatus getInstance() {
        if (instance == null) {
            instance = new RobotStatus();
        }
        return instance;
    }

    private String currentScene = ""; //当前场景
    private long beginSpeakTime;//开始说话时刻
    private boolean isSpeaking;//true 表示正在说话，false 表示停止说话
    private long endSpeakTime;//结束说话时刻

    private boolean currentNavEnd = false; //   导航完成状态
    private boolean currentNavWakeup = false; //导航唤醒状态
    private boolean currentVisit = false;//当前参观展厅状态
    private boolean currentVisitWakeup = false;//当前参观展厅被唤醒状态
    private String nextVisitPoint = "";    //参观展厅时下一导航地点
    private String nextVisitAnswer = "";//参观展厅时去下一导航介绍语

    public boolean isCurrentVisit() {
        return currentVisit;
    }

    public void setCurrentVisit(boolean currentVisit) {
        this.currentVisit = currentVisit;
    }

    public boolean isCurrentVisitWakeup() {
        return currentVisitWakeup;
    }

    public void setCurrentVisitWakeup(boolean currentVisitWakeup) {
        this.currentVisitWakeup = currentVisitWakeup;
    }

    public String getNextVisitPoint() {
        return nextVisitPoint;
    }

    public void setNextVisitPoint(String nextVisitPoint) {
        this.nextVisitPoint = nextVisitPoint;
    }

    public String getNextVisitAnswer() {
        return nextVisitAnswer;
    }

    public void setNextVisitAnswer(String nextVisitAnswer) {
        this.nextVisitAnswer = nextVisitAnswer;
    }


    public String getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(String currentScene) {
        this.currentScene = currentScene;
    }


    public long getBeginSpeakTime() {
        return beginSpeakTime;
    }

    public void setBeginSpeakTime(long beginSpeakTime) {
        this.beginSpeakTime = beginSpeakTime;
    }

    public boolean isSpeaking() {
        return isSpeaking;
    }

    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }

    public long getEndSpeakTime() {
        return endSpeakTime;
    }

    public void setEndSpeakTime(long endSpeakTime) {
        this.endSpeakTime = endSpeakTime;
    }

    public boolean isCurrentNavEnd() {
        return currentNavEnd;
    }

    public void setCurrentNavEnd(boolean currentNavEnd) {
        this.currentNavEnd = currentNavEnd;
    }

    public boolean isCurrentNavWakeup() {
        return currentNavWakeup;
    }

    public void setCurrentNavWakeup(boolean currentNavWakeup) {
        this.currentNavWakeup = currentNavWakeup;
    }


}
