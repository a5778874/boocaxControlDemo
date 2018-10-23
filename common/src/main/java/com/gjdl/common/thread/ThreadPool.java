package com.gjdl.common.thread;



public class ThreadPool {

    public static void execute(Runnable task){

        FixTreadPoolOperator.getPool().execute(task);
    }


}
