package com.fewok.lib.process.flow;

import com.fewok.lib.process.container.ProcessResult;
import com.fewok.lib.process.entity.*;
import com.fewok.lib.process.execute.ExecutorProcess;
import com.fewok.lib.process.AbstractBaseFlowProcessor;
import com.fewok.lib.process.TestResource;
import com.fewok.lib.process.entity.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author notreami on 18/7/7.
 */
@Slf4j
public class TestFlow extends AbstractBaseFlowProcessor<TestRequestData, TestResponseData> {

    @Override
    protected CommonOutput<TestResponseData> doProcess(CommonInput<TestRequestData> commonInput) throws Exception {
        TestHolderData testHolderData = new TestHolderData();
        CommonOutput<TestResponseData> commonOutput = new CommonOutput<>();
        commonOutput.setData(new TestResponseData());

        PContext<CommonInput<TestRequestData>, TestHolderData, CommonOutput<TestResponseData>> processContext =
                new PContext<>(commonInput, testHolderData, commonOutput);

        long time = System.currentTimeMillis();
        ProcessResult processResult = ExecutorProcess.execute(processContext, TestResource.executeContext);
        log.info("执行耗费时间：{}", System.currentTimeMillis() - time);
        if (!processResult.isSuccess()) {
            log.error("执行失败 message={}", processResult.getExecuteStatus().getMessage());
        }
        return processContext.getCommonOutput();
    }
}
