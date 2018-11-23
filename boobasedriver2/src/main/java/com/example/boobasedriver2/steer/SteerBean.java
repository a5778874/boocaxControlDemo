package com.example.boobasedriver2.steer;

public class SteerBean {
    private int angle;
    private int speed;
    private int controlBody;

    public SteerBean(int angle, int speed, int controlBody) {
        this.angle = angle;
        this.speed = convertSpeed(speed);
        this.controlBody = controlBody;
    }

    public SteerBean() {
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = convertSpeed(speed);
    }

    //速度判断，限制传入的速度参数1-15
    private int convertSpeed(int speed) {
        int sp;
        if (speed < 1) {
            sp = 1;
        } else if (speed > 15) {
            sp = 15;
        } else {
            sp = speed;
        }
        return sp;
    }

    public int getControlBody() {
        return controlBody;
    }

    public void setControlBody(int controlBody) {
        this.controlBody = controlBody;
    }


    @Override
    public String toString() {
        return "SteerBean{" +
                "angle=" + angle +
                ", speed=" + speed +
                ", controlBody='" + controlBody + '\'' +
                '}';
    }
}
