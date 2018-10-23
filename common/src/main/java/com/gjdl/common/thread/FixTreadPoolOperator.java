package com.gjdl.common.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zxos on 2017/3/18.
 */

public class FixTreadPoolOperator {

    //核心线程数 = 最大线程数，只有核心线程并且这些核心线程不会被回收，这意味着它能够更加快速地响应外界的请求。
    private static ExecutorService fixThreadPool;

    public static Executor getPool(){
        if (fixThreadPool == null){
            synchronized (FixTreadPoolOperator.class){
                if (fixThreadPool == null)
                    fixThreadPool = Executors.newFixedThreadPool(6);
            }
        }
        return fixThreadPool;
    }

    public static void runOnThread(Runnable task){
        getPool().execute(task);
    }


}
