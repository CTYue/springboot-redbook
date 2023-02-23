package com.chuwa.redbook.cron;

import java.util.Date;

/**
 * @author b1go
 * @date 2/22/23 5:15 PM
 */
class RunnableTask implements Runnable{
    private String message;

    public RunnableTask(String message){
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println(new Date()+" Runnable Task with "+message
                +" on thread "+Thread.currentThread().getName());
    }
}
