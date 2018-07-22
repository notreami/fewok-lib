package com.fewok.lib.process.execute;

import com.fewok.lib.process.container.ProcessContext;
import com.fewok.lib.process.ProcessExecutor;
import com.fewok.lib.process.Processor;
import com.fewok.lib.process.container.ExecuteContext;
import com.fewok.lib.process.container.ExecuteResult;
import com.fewok.lib.process.type.InvokeType;
import com.fewok.lib.process.util.JsonProcess;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 执行处理（发起执行、获取执行结果）
 *
 * @author notreami on 18/7/7.
 */
@Slf4j
public class ExecutorHandler {

    /**
     * 执行activityProcessor
     *
     * @param processContext
     * @param executeInfo
     * @param processExecutorMap
     */
    static void executeProcessor(ProcessContext processContext, ExecuteContext.ExecuteInfo executeInfo, Map<InvokeType, ProcessExecutor> processExecutorMap) {
        Processor processor = executeInfo.getActivityProcessor();
        InvokeType invokeType = executeInfo.getExecuteRule().getInvokeType();

        executeInfo.setFuture(processExecutorMap.get(invokeType).execute(processor, processContext));
        executeInfo.setExecuteCount(executeInfo.getExecuteCount() + 1);
    }

    /**
     * 获取执行结果（无future，则返回上次获取的结果）
     *
     * @param processContext
     * @param executeInfo
     * @return
     */
    static ExecuteResult getExecuteResult(ProcessContext processContext, ExecuteContext.ExecuteInfo executeInfo) {
        Processor processor = executeInfo.getActivityProcessor();
        Future<ExecuteResult> future = executeInfo.getFuture();
        if (future == null) {
            return executeInfo.getExecuteResult();
        }

        ExecuteResult executeResult = null;
        try {
            executeResult = future.get(executeInfo.getExecuteRule().getTimeoutMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException | InterruptedException e) {
            log.error("获取Process执行结果超时异常,processor={},processContext={}", processor.getProcessorName(), JsonProcess.toJSONString(processContext), e);
            executeResult = ExecuteResult.TIMEOUT_ERROR;
        } catch (Exception e) {
            log.error("获取Process执行结果异常,processor={},processContext={}", processor.getProcessorName(), JsonProcess.toJSONString(processContext), e);
        }

        if (executeResult == null) {
            executeResult = ExecuteResult.UNKNOWN_ERROR;
        }

        executeInfo.setFuture(null);
        executeInfo.setExecuteResult(executeResult);
        return executeResult;
    }
}
