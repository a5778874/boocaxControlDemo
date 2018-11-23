package com.example.boobasedriver2.boobase.event;

/**
 * create by zzh on 2018/8/9
 */
public class MoveStatus {

    /**
     * 静止待命中（开机待命状态）
     */
    public static final int BOOCAX_MOVE_STOPPING = 0;
    /**
     * (导航结束状态）上次目标失败，等待新的导航命令
     */
    public static final int BOOCAX_MOVE_NAVIGATION_FAILED = 1;
    /**
     * (导航结束状态）上次目标完成，等待断的导航命令
     */
    public static final int BOOCAX_MOVE_NAVIGATION_SUCCESS = 2;
    /**
     * （导航中状态）移动中，正在前往目的地
     */
    public static final int BOOCAX_MOVE_MOVING = 3;
    /**
     * (导航中状态）前方障碍物
     */
    public static final int BOOCAX_MOVE_HAVING_BARRIER = 4;
    /**
     * （导航中状态）目的地被遮档
     */
    public static final int BOOCAX_MOVE_DESTINATION_BLOCKED = 5;
    /**
     * (导航结束状态) 导航取消 (用户主动取消）
     */
    public static final int BOOCAX_MOVE_NAVIGATION_CANCEL = 6;
    /**
     * 新目标点;每次收到导航目标点都会返回此状态，然后再切换到其他状态；
     */
    public static final int BOOCAX_MOVE_NEW_GOAL = 7;
    /**
     * （导航中状态）导航路径阻塞；机器人长时间(30秒）保持状态 4（前方防碍物）/ 5(目的地被遮挡）会切换到状态8,一段时间没有新指令则切换到状态1（导航失败）；
     */
    public static final int BOOCAX_MOVE_NAVIGATION_BLOCKED = 8;

    private int msg;

    public MoveStatus() {
    }

    public MoveStatus(int msg) {
        this.msg = msg;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }



}
