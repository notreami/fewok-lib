package com.fewok.lib.process.activity;

import com.fewok.lib.process.AbstractActivityProcessor;
import com.fewok.lib.process.container.ExecuteResult;
import com.fewok.lib.process.entity.*;
import com.fewok.lib.process.entity.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author notreami on 18/7/7.
 */
@Slf4j
public class FTestActivity extends AbstractActivityProcessor<TestRequestData, TestHolderData, TestResponseData> {

    @Override
    protected ExecuteResult doProcess(PContext<CommonInput<TestRequestData>, TestHolderData, CommonOutput<TestResponseData>> processContext) throws Exception {
        long start = System.currentTimeMillis();
        log.info("当前Activity={},线程名={},开始执行时间={}", getProcessorName(), Thread.currentThread().getName(), start);
        Thread.sleep(100);
        long end = System.currentTimeMillis();
        log.info("当前Activity={},线程名={},执行结束时间={},耗时={}", getProcessorName(), Thread.currentThread().getName(), end, end - start);
        return ExecuteResult.OK;
    }
}
