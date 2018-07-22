package com.fewok.lib.process;

import com.fewok.lib.process.container.ExecuteContext;
import com.fewok.lib.process.container.ExecuteRule;
import com.fewok.lib.process.activity.ATestActivity;
import com.fewok.lib.process.activity.BTestActivity;
import com.fewok.lib.process.activity.CTestActivity;
import com.fewok.lib.process.type.InvokeType;
import com.fewok.lib.process.util.JsonProcess;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author notreami on 18/7/7.
 */
@Slf4j
public class SimpleProcessTest {
    private static ExecuteContext aTestExecuteContext;
    private static ExecuteContext bTestExecuteContext;
    private static ExecuteContext cTestExecuteContext;
    private static ExecuteContext dTestExecuteContext;
    private static ExecuteContext eTestExecuteContext;
    private static ExecuteContext fTestExecuteContext;
    private static ExecuteContext gTestExecuteContext;
    private static ExecuteContext hTestExecuteContext;

    static {
        aTestExecuteContext = ExecuteContext.builder()
                .fromExecutor(TestResource.EXECUTOR_SERVICE)
                .then(new ATestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new BTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new CTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .build();

        bTestExecuteContext = ExecuteContext.builder()
                .fromExecutor(TestResource.EXECUTOR_SERVICE)
                .then(new ATestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new BTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new CTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .build();

        cTestExecuteContext = ExecuteContext.builder()
                .fromExecutor(TestResource.EXECUTOR_SERVICE)
                .then(new ATestActivity(), ExecuteRule.builder().invokeType(InvokeType.PARALLEL).build())
                .then(new BTestActivity(), ExecuteRule.builder().invokeType(InvokeType.PARALLEL).build())
                .then(new CTestActivity(), ExecuteRule.builder().invokeType(InvokeType.PARALLEL).build())
                .build();

        dTestExecuteContext = ExecuteContext.builder()
                .fromExecutor(TestResource.EXECUTOR_SERVICE)
                .then(new ATestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new BTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new CTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .build();

        eTestExecuteContext = ExecuteContext.builder()
                .fromExecutor(TestResource.EXECUTOR_SERVICE)
                .then(new ATestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new BTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new CTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .build();

        fTestExecuteContext = ExecuteContext.builder()
                .fromExecutor(TestResource.EXECUTOR_SERVICE)
                .then(new ATestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new BTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new CTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .build();

        gTestExecuteContext = ExecuteContext.builder()
                .fromExecutor(TestResource.EXECUTOR_SERVICE)
                .then(new ATestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new BTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .then(new CTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .build();

        hTestExecuteContext = ExecuteContext.builder()
                .fromExecutor(TestResource.EXECUTOR_SERVICE)
                .then(new ATestActivity(), ExecuteRule.builder().invokeType(InvokeType.PARALLEL).build())
                .then(new BTestActivity(), ExecuteRule.builder().invokeType(InvokeType.ASYNC).build())
                .then(new CTestActivity(), ExecuteRule.builder().invokeType(InvokeType.SYNC).build())
                .build();
    }

    @BeforeClass
    public void init() {
        TestResource.initThreadPool();
    }

    @Test(priority = 1)
    public void testCase1() {
        log.info("同步->同步->同步");
        TestResource.executeContext = aTestExecuteContext;
        TestResource.integerList.forEach(x -> {
            long time = System.currentTimeMillis();
            TestResource.commonOutput = TestResource.testFlow.process(TestResource.commonInput);
            log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        });
        log.info("同步->同步->同步：commonOutput={}", JsonProcess.toJSONString(TestResource.commonOutput));
    }

    @Test(priority = 2)
    public void testCase2() {
        log.info("异步->异步->异步");
        TestResource.executeContext = bTestExecuteContext;
        TestResource.integerList.forEach(x -> {
            long time = System.currentTimeMillis();
            TestResource.commonOutput = TestResource.testFlow.process(TestResource.commonInput);
            log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        });
        log.info("异步->异步->异步：commonOutput={}", JsonProcess.toJSONString(TestResource.commonOutput));
    }

    @Test(priority = 3)
    public void testCase3() {
        log.info("并行->并行->并行");
        TestResource.executeContext = cTestExecuteContext;
        TestResource.integerList.forEach(x -> {
            long time = System.currentTimeMillis();
            TestResource.commonOutput = TestResource.testFlow.process(TestResource.commonInput);
            log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        });
        log.info("并行->并行->并行：commonOutput={}", JsonProcess.toJSONString(TestResource.commonOutput));
    }

    @Test(priority = 4)
    public void testCase4() {
        log.info("同步->同步->异步");
        TestResource.executeContext = dTestExecuteContext;
        TestResource.integerList.forEach(x -> {
            long time = System.currentTimeMillis();
            TestResource.commonOutput = TestResource.testFlow.process(TestResource.commonInput);
            log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        });
        log.info("同步->同步->异步：commonOutput={}", JsonProcess.toJSONString(TestResource.commonOutput));
    }

    @Test(priority = 5)
    public void testCase5() {
        log.info("同步->异步->异步");
        TestResource.executeContext = eTestExecuteContext;
        TestResource.integerList.forEach(x -> {
            long time = System.currentTimeMillis();
            TestResource.commonOutput = TestResource.testFlow.process(TestResource.commonInput);
            log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        });
        log.info("同步->异步->异步：commonOutput={}", JsonProcess.toJSONString(TestResource.commonOutput));
    }

    @Test(priority = 6)
    public void testCase6() {
        log.info("异步->异步->同步");
        TestResource.executeContext = fTestExecuteContext;
        TestResource.integerList.forEach(x -> {
            long time = System.currentTimeMillis();
            TestResource.commonOutput = TestResource.testFlow.process(TestResource.commonInput);
            log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        });
        log.info("异步->异步->同步：commonOutput={}", JsonProcess.toJSONString(TestResource.commonOutput));
    }

    @Test(priority = 7)
    public void testCase7() {
        log.info("异步->同步->同步");
        TestResource.executeContext = gTestExecuteContext;
        TestResource.integerList.forEach(x -> {
            long time = System.currentTimeMillis();
            TestResource.commonOutput = TestResource.testFlow.process(TestResource.commonInput);
            log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        });
        log.info("异步->同步->同步：commonOutput={}", JsonProcess.toJSONString(TestResource.commonOutput));
    }

    @Test(priority = 8)
    public void testCase8() {
        log.info("并行->异步->同步");
        TestResource.executeContext = hTestExecuteContext;
        TestResource.integerList.forEach(x -> {
            long time = System.currentTimeMillis();
            TestResource.commonOutput = TestResource.testFlow.process(TestResource.commonInput);
            log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        });
        log.info("并行->异步->同步：commonOutput={}", JsonProcess.toJSONString(TestResource.commonOutput));
    }
}
