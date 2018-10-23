package com.example.boobasedriver2.boobase.entity;

/**
 * create by zzh on 2018/10/23
 */
public class LocationEntity {


    /**
     * name : 办公室
     * coordinate : {"x":-11.15,"y":10.45,"yaw":-94.11}
     * arm : left
     * reply : 从电梯上去5楼后，509就是我们的办公室
     * nextNav : 洗手间
     * nextAnswer :
     */

    private String name;
    private CoordinateBean coordinate;
    private String arm;
    private String reply;
    private String nextNav;
    private String nextAnswer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoordinateBean getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(CoordinateBean coordinate) {
        this.coordinate = coordinate;
    }

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getNextNav() {
        return nextNav;
    }

    public void setNextNav(String nextNav) {
        this.nextNav = nextNav;
    }

    public String getNextAnswer() {
        return nextAnswer;
    }

    public void setNextAnswer(String nextAnswer) {
        this.nextAnswer = nextAnswer;
    }

    public static class CoordinateBean {
        /**
         * x : -11.15
         * y : 10.45
         * yaw : -94
         */

        private float x;
        private float y;
        private float yaw;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getYaw() {
            return yaw;
        }

        public void setYaw(int yaw) {
            this.yaw = yaw;
        }

        @Override
        public String toString() {
            return "CoordinateBean{" +
                    "x=" + x +
                    ", y=" + y +
                    ", yaw=" + yaw +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LocationEntity{" +
                "name='" + name + '\'' +
                ", coordinate=" + coordinate +
                ", arm='" + arm + '\'' +
                ", reply='" + reply + '\'' +
                ", nextNav='" + nextNav + '\'' +
                ", nextAnswer='" + nextAnswer + '\'' +
                '}';
    }
}
