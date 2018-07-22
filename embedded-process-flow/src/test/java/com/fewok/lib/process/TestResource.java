package com.fewok.lib.process;

import com.fewok.lib.process.container.ExecuteContext;
import com.fewok.lib.process.entity.CommonInput;
import com.fewok.lib.process.entity.CommonOutput;
import com.fewok.lib.process.flow.TestFlow;
import com.fewok.lib.process.entity.TestRequestData;
import com.fewok.lib.process.entity.TestResponseData;
import com.fewok.lib.process.util.IdGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author notreami on 18/7/7.
 */
@Slf4j
public class TestResource {
    public static CommonInput<TestRequestData> commonInput = new CommonInput<>();
    public static CommonOutput<TestResponseData> commonOutput = null;

    //用于 IdWorker 初始化
    private static final IdGenerator ID_GENERATOR = new IdGenerator();
    public static List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

    public static ExecuteContext executeContext;
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);
    public static final TestFlow testFlow = new TestFlow();

    public static void initThreadPool() {
        long time = System.currentTimeMillis();
        List<Future> futureList = new ArrayList<>(12);
        integerList.forEach(x -> {
            Future future = EXECUTOR_SERVICE.submit(() -> {
                log.info("初始化线程池 线程名={}", Thread.currentThread().getName());
                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
            futureList.add(future);
        });
        futureList.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        log.info("初始化线程池 耗费时间：{}", System.currentTimeMillis() - time);
    }

}
