package com.fewok.lib.process.processor;

import com.fewok.lib.process.container.ProcessContext;
import com.fewok.lib.process.container.ExecuteResult;
import com.fewok.lib.process.type.ExecuteStatus;
import com.fewok.lib.process.util.JsonProcess;
import lombok.extern.slf4j.Slf4j;

/**
 * 活动流程
 *
 * @author notreami on 18/7/7.
 */
@Slf4j
public abstract class BaseActivityProcessor<P extends ProcessContext> extends BaseProcessor<P, ExecuteResult> {

    @Override
    protected ExecuteResult handleCheckFail(P processContext, ExecuteResult executeResult) {
        return executeResult;
    }

    @Override
    protected ExecuteResult handleException(P processContext, ExecuteResult executeResult, Exception e) {
        log.error("Process[{}]处理异常,processId={},processContext={}", getProcessorName(), processContext.getProcessId(), JsonProcess.toJSONString(processContext), e);
        if (executeResult == null) {
            executeResult = ExecuteResult.builder().build();
        }
        executeResult.setSuccess(false);
        executeResult.setExecuteStatus(ExecuteStatus.UNKNOWN_ERROR);
        executeResult.setException(e);
        return executeResult;
    }
}
