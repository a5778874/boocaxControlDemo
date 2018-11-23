package com.example.boobasedriver2.boobase;

public abstract class BaseControllor {
    /**
     * 根据控制参数生成独立标记码，用于避免生成同样的控制码
     */
    protected int generalFlag(Object... params) {
        int flag = -1;
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof String) {
                flag += params[i].hashCode();
            } else if (params[i] instanceof Integer) {
                flag += (Integer) params[i] * Math.pow(10, i + 1);
            } else if (params[i] instanceof Float) {
                flag += (float) params[i] * Math.pow(20, i + 1);
            } else if (params[i] instanceof Boolean) {
                flag = (int) ((boolean) params[i] ? flag + Math.pow(30, i + 1) : flag + Math.pow(40, i + 1));
            } else {
                flag += (Integer) params[i] * Math.pow(60, i + 1);
            }
        }
        return flag;
    }
}
