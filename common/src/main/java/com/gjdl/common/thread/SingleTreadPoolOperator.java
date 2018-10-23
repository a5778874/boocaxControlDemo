package com.gjdl.common.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zxos on 2017/3/18.
 */

public class SingleTreadPoolOperator {

    //单线程池
    private static ExecutorService singleThreadPool;

    public static Executor getPool(){
        if (singleThreadPool == null){
            synchronized (SingleTreadPoolOperator.class){
                if (singleThreadPool == null)
                    singleThreadPool = Executors.newSingleThreadExecutor();
            }
        }
        return singleThreadPool;
    }

    public static void runOnThread(Runnable task){
        getPool().execute(task);
    }


}
