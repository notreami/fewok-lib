package com.fewok.lib.process;

import com.fewok.lib.process.activity.*;
import com.fewok.lib.process.container.ExecuteContext;
import com.fewok.lib.process.container.ExecuteRule;
import com.fewok.lib.process.activity.*;
import com.fewok.lib.process.type.InvokeType;
import com.fewok.lib.process.util.JsonProcess;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author notreami on 18/7/7.
 */
@Slf4j
public class LongProcessTest {
    private static ExecuteContext iTestExecuteContext;
    private static ExecuteContext jTestExecuteContext;
    private static ExecuteContext kTestExecuteContext;
    private static ExecuteContext lTestExecuteContext;

    static {
        iTestExecuteContext = ExecuteContext.builder()
                .fromExecutor(TestResource.EXECUTOR_SERVICE)
                .then(new ATestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new BTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new CTestActivity(), ExecuteRule.builder().invokeType(InvokeType.PARALLEL).build())
                .then(new DTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new ETestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).isWaitBeforeProcessor(true).build())
                .then(new FTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new GTestActivity(), ExecuteRule.builder().invokeType(InvokeType.PARALLEL).build())
                .then(new HTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).isWaitBeforeProcessor(true).build())
                .build();

        jTestExecuteContext = ExecuteContext.builder()
                .fromExecutor(TestResource.EXECUTOR_SERVICE)
                .then(new ATestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new BTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new CTestActivity(), ExecuteRule.builder().invokeType(InvokeType.PARALLEL).build())
                .then(new DTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new ETestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new FTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new GTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new HTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .build();

        kTestExecuteContext = ExecuteContext.builder()
                .fromExecutor(TestResource.EXECUTOR_SERVICE)
                .then(new ATestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new BTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new CTestActivity(), ExecuteRule.builder().invokeType(InvokeType.PARALLEL).build())
                .then(new DTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new ETestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new FTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new GTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new HTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).timeoutMillis(3).build())
                .build();

        lTestExecuteContext = ExecuteContext.builder()
                .fromExecutor(TestResource.EXECUTOR_SERVICE)
                .then(new ATestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new BTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new CTestActivity(), ExecuteRule.builder().invokeType(InvokeType.PARALLEL).build())
                .then(new DTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new ETestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new FTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new HTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).timeoutMillis(3).build())
                .build();
    }

    @BeforeClass
    public void init() {
        TestResource.initThreadPool();
    }

    @Test(priority = 9)
    public void testCase9() {
        log.info("同步->异步->并行->同步->阻塞同步->异步->并行->阻塞同步");
        TestResource.executeContext = iTestExecuteContext;
        TestResource.integerList.forEach(x -> {
            long time = System.currentTimeMillis();
            TestResource.commonOutput = TestResource.testFlow.process(TestResource.commonInput);
            log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        });
        log.info("同步->异步->并行->同步->阻塞同步->异步->并行->阻塞同步：(TestResource.commonOutput={}", JsonProcess.toJSONString(TestResource.commonOutput));
    }

    @Test(priority = 10)
    public void testCase10() {
        log.info("同步->异步->并行->同步->同步->异步->异步->同步");
        TestResource.executeContext = jTestExecuteContext;
        TestResource.integerList.forEach(x -> {
            long time = System.currentTimeMillis();
            TestResource.commonOutput = TestResource.testFlow.process(TestResource.commonInput);
            log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        });
        log.info("同步->异步->并行->同步->同步->异步->异步->同步：(TestResource.commonOutput={}", JsonProcess.toJSONString(TestResource.commonOutput));
    }

    @Test(priority = 11)
    public void testCase11() {
        log.info("同步->异步->并行->同步->同步->异步->异步->同步执行错误");
        TestResource.executeContext = kTestExecuteContext;
        TestResource.integerList.forEach(x -> {
            long time = System.currentTimeMillis();
            TestResource.commonOutput = TestResource.testFlow.process(TestResource.commonInput);
            log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        });
        log.info("同步->异步->并行->同步->同步->异步->异步->同步执行错误：(TestResource.commonOutput={}", JsonProcess.toJSONString(TestResource.commonOutput));
    }

    @Test(priority = 12)
    public void testCase12() {
        log.info("同步->异步->并行->同步->同步->异步->异步执行错误");
        TestResource.executeContext = lTestExecuteContext;
        TestResource.integerList.forEach(x -> {
            long time = System.currentTimeMillis();
            TestResource.commonOutput = TestResource.testFlow.process(TestResource.commonInput);
            log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        });
        log.info("同步->异步->并行->同步->同步->异步->异步执行错误：(TestResource.commonOutput={}", JsonProcess.toJSONString(TestResource.commonOutput));
    }
}
