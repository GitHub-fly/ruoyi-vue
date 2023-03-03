package com.ruoyi.system;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @className Producer
 */
public class Producer {

    public static void main(String[] args) {
        Producer.createData();
    }

    public static void createData() {
        ExecutorService pool = Executors.newFixedThreadPool(100);
        final int totalPageNo = 50; //分50批次

        final int pageSize = 20000; //每页大小是2万条
        //共10w条数据，每页5000条数据，20个线程
        final long start = System.currentTimeMillis();
        final AtomicInteger atomicInt = new AtomicInteger();
        for (int currentPageNo = 0; currentPageNo < totalPageNo; currentPageNo++) {
            final int finalCurrentPageNo = currentPageNo;

            Runnable run = new Runnable() {

                @Override
                public void run() {
                    List userList = new ArrayList<>();
                    for (int i = 1; i <= pageSize; i++) {
                        int id = i + finalCurrentPageNo * pageSize;
                    }

//					atomicInt.addAndGet(UserBatchHandler.batchSave(userList,Thread.currentThread().getName()));
                    //入库的数据达到一百万条的时候就会有个统计.
                    if (atomicInt.get() == (totalPageNo * pageSize)) {
                        //如果有一百万的时候.就会在这里有个结果
                        System.out.println(atomicInt.get());
                        System.out.println("同步数据到db，它已经花费 " + (System.currentTimeMillis() - start) + "  ms");
                    }
                }
            };
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pool.execute(run);
            System.out.println("sleep======");
        }

    }

}
