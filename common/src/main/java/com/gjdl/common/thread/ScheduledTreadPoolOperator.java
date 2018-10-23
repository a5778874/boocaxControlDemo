package com.gjdl.common.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zxos on 2017/3/18.
 */

public class ScheduledTreadPoolOperator {

    //核心线程数 = count ， 最大线程数 = 最大，只要用于执行定时任务和具有固定周期的重复任务。
    private static ExecutorService scheduledThreadPool;

    public static Executor getPool(){
        if (scheduledThreadPool == null){
            synchronized (ScheduledTreadPoolOperator.class){
                if (scheduledThreadPool == null)
                    scheduledThreadPool = Executors.newScheduledThreadPool(6);
            }
        }
        return scheduledThreadPool;
    }

    public static void runOnThread(Runnable task){
        getPool().execute(task);
    }


}
