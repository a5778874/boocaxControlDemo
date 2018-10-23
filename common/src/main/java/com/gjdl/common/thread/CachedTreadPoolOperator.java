package com.gjdl.common.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zxos on 2017/3/18.
 */

public class CachedTreadPoolOperator {

    //核心线程数 = 0 ， 最大线程数 = 最大，比较适合执行大量的耗时较少的任务
    private static ExecutorService cachedThreadPool;

    public static Executor getPool(){
        if (cachedThreadPool == null){
            synchronized (CachedTreadPoolOperator.class){
                if (cachedThreadPool == null)
                    cachedThreadPool = Executors.newCachedThreadPool();
            }
        }
        return cachedThreadPool;
    }

    public static void runOnThread(Runnable task){
        getPool().execute(task);
    }


}
