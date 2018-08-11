package com.example.boobasedriver.eventbus;

/**
 * create by zzh on 2018/8/9
 */
public class MoveStatus {
    /**
     * 从底盘发送过来的未处理原命令
     */
    private byte [] respondCmd;

    /**
     * 移动状态
     * 0-静止待命中（开机待命状态）；
     * 1-(导航结束状态）上次目标失败，等待新的导航命令；
     * 2-(导航结束状态）上次目标完成，等待断的导航命令；
     * 3-（导航中状态）移动中，正在前往目的地；
     * 4-(导航中状态）前方障碍物；
     * 5-（导航中状态）目的地被遮档；
     * 6-(导航结束状态) 导航取消 (用户主动取消）；
     * 7-新目标点;每次收到导航目标点都会返回此状态，然后再切换到其他状态；
     * 8-（导航中状态）导航路径阻塞；机器人长时间(30秒）保持状态 4（前方防碍物）/ 5(目的地被遮挡）会切换到状态8,一段时间没有新指令则切换到状态1（导航失败）；
     */
    private int moveStatus;

    public MoveStatus(byte[] respondCmd, int moveStatus) {
        this.respondCmd = respondCmd;
        this.moveStatus = moveStatus;
    }

    public byte[] getRespondCmd() {
        return respondCmd;
    }

    public void setRespondCmd(byte[] respondCmd) {
        this.respondCmd = respondCmd;
    }

    public int getMoveStatus() {
        return moveStatus;
    }

    public void setMoveStatus(int moveStatus) {
        this.moveStatus = moveStatus;
    }
}
