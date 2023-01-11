package com.sunlent.iot.plc.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author aborn (jiangguobao)
 * @date 2023/01/06 14:48
 */
public class ExecutorExample {
    ExecutorService executor = Executors.newFixedThreadPool(10);

    public void useShutdown() {
        Runnable runnableTask = () -> {
            try {
                for (int i = 1; i < 100; i++) {
                    System.out.println("Thread ID:" + Thread.currentThread().getName() + "I am running, i=" + i);
                    TimeUnit.MILLISECONDS.sleep(300);
                }
            } catch (InterruptedException e) {
                System.out.println("exception.");
                e.printStackTrace();
            }
        };

        executor.execute(runnableTask);
        executor.execute(runnableTask);
        System.out.println("executor status: " + executor.isShutdown());
        // executor.awaitTermination(800, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        executor.shutdownNow();
        System.out.println("executor status: " + executor.isShutdown());
    }


    public static void main(String[] args) {
        ExecutorExample executorExample = new ExecutorExample();
        executorExample.useShutdown();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            System.out.println("fff");
        }
        executorExample.stop();
    }
}
