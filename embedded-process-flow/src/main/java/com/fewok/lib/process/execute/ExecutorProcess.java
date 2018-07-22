package com.fewok.lib.process.execute;

import com.fewok.lib.process.container.ProcessContext;
import com.fewok.lib.process.ProcessExecutor;
import com.fewok.lib.process.container.ExecuteContext;
import com.fewok.lib.process.container.ExecuteResult;
import com.fewok.lib.process.container.ProcessResult;
import com.fewok.lib.process.type.InvokeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * 执行流程编制
 *
 * @author notreami on 18/7/7.
 */
@Slf4j
public class ExecutorProcess {

    public static ProcessResult execute(ProcessContext processContext, ExecuteContext executeContext) throws Exception {
        executeContext = executeContext.copy(executeContext);
        ExecuteContext.ExecuteContainer executeContainer = executeContext.getExecuteContainer();
        Map<InvokeType, ProcessExecutor> processExecutorMap = executeContext.getProcessExecutorMap();
        if (executeContainer == null || CollectionUtils.isEmpty(executeContainer.getExecuteInfoList())) {
            return ProcessResult.SKIP;
        }
        circleArrangeExecute(processContext, executeContainer, processExecutorMap);

        if (!executeContainer.isAllSuccess() && MapUtils.isNotEmpty(executeContext.getReviseExecuteContainerMap())) {
            ExecuteContext.ExecuteInfo executeInfo;
            ExecuteResult executeResult;
            ExecuteContext.ExecuteContainer reviseExecuteContainer;
            for (int i = 0; i < executeContainer.getEndIndex(); i++) {
                executeInfo = executeContainer.getExecuteInfoList().get(i);
                executeResult = executeInfo.getExecuteResult();
                if (executeResult == null || executeResult.isSuccess()) {
                    continue;
                }
                //多个执行失败,失败process都会执行恢复
                reviseExecuteContainer = executeContext.getReviseExecuteContainerMap().get(i);
                if (reviseExecuteContainer == null || CollectionUtils.isEmpty(reviseExecuteContainer.getExecuteInfoList())) {
                    continue;
                }
                circleArrangeExecute(processContext, reviseExecuteContainer, processExecutorMap);
            }
        }
        return mergeExecuteResult(executeContext.getExecuteContainer());
    }

    private static void circleArrangeExecute(ProcessContext processContext, ExecuteContext.ExecuteContainer executeContainer, Map<InvokeType, ProcessExecutor> processExecutorMap) {
        while (executeContainer.isAllSuccess() && executeContainer.getEndIndex() < executeContainer.getExecuteInfoList().size()) {
            executeContainerExecute(processContext, executeContainer, processExecutorMap);
            executeContainer.setEndIndex(executeContainer.getEndIndex() + 1);
            if (!executeContainer.isOnlySync()) {
                waitExecuteResultAndRetry(processContext, executeContainer, processExecutorMap);
                executeContainer.setOnlySync(true);
            }
        }
    }

    private static void executeContainerExecute(ProcessContext processContext, ExecuteContext.ExecuteContainer executeContainer, Map<InvokeType, ProcessExecutor> processExecutorMap) {
        ExecuteContext.ExecuteInfo executeInfo;
        ExecuteResult executeResult;
        executeContainer.setBeginIndex(executeContainer.getEndIndex());
        for (int i = executeContainer.getBeginIndex(); i < executeContainer.getExecuteInfoList().size(); i++) {
            executeInfo = executeContainer.getExecuteInfoList().get(i);
            if (!executeContainer.isOnlySync() && executeInfo.getExecuteRule().isWaitBeforeProcessor() && i != executeContainer.getBeginIndex()) {
                break;
            }

            executeContainer.setEndIndex(i);
            ExecutorHandler.executeProcessor(processContext, executeInfo, processExecutorMap);
            if (executeInfo.getExecuteRule().getInvokeType() != InvokeType.SYNC) {
                if (executeContainer.isOnlySync()) {
                    executeContainer.setOnlySync(false);
                }
                continue;
            }

            executeResult = ExecutorHandler.getExecuteResult(processContext, executeInfo);
            if (executeResult != null && executeResult.isSuccess()) {
                continue;
            }

            for (int r = 1; r < executeInfo.getExecuteTotal(); r++) {
                ExecutorHandler.executeProcessor(processContext, executeInfo, processExecutorMap);
                executeResult = ExecutorHandler.getExecuteResult(processContext, executeInfo);
                if (executeResult != null && executeResult.isSuccess()) {
                    break;
                }
            }
            if (executeResult == null || !executeResult.isSuccess()) {
                //中断
                executeContainer.setAllSuccess(false);
                break;
            }
        }
    }

    private static void waitExecuteResultAndRetry(ProcessContext processContext, ExecuteContext.ExecuteContainer executeContainer, Map<InvokeType, ProcessExecutor> processExecutorMap) {
        boolean isFinish = true;

        ExecuteContext.ExecuteInfo executeInfo;
        for (int i = executeContainer.getBeginIndex(); i < executeContainer.getEndIndex(); i++) {
            executeInfo = executeContainer.getExecuteInfoList().get(i);
            if (executeInfo.getExecuteRule().getInvokeType() == InvokeType.SYNC) {
                continue;
            }

            ExecuteResult executeResult = ExecutorHandler.getExecuteResult(processContext, executeInfo);
            if (executeResult == null || executeResult.isSuccess()) {
                continue;
            }
            if (executeInfo.getExecuteCount() >= executeInfo.getExecuteTotal()) {
                if (executeContainer.isAllSuccess()) {
                    executeContainer.setAllSuccess(false);
                }
                continue;
            }

            isFinish = false;
            ExecutorHandler.executeProcessor(processContext, executeInfo, processExecutorMap);
        }

        if (!isFinish) {
            waitExecuteResultAndRetry(processContext, executeContainer, processExecutorMap);
        }
    }

    private static ProcessResult mergeExecuteResult(ExecuteContext.ExecuteContainer executeContainer) {
        if (executeContainer.isAllSuccess()) {
            return ProcessResult.OK;
        }
        ExecuteResult executeResult = null;
        ExecuteContext.ExecuteInfo executeInfo;
        for (int i = executeContainer.getEndIndex() - 1; i >= 0; i--) {
            executeInfo = executeContainer.getExecuteInfoList().get(i);
            ExecuteResult temp = executeInfo.getExecuteResult();
            if (temp != null && !temp.isSuccess()) {
                executeResult = temp;
                break;
            }
        }

        if (executeResult == null) {
            return ProcessResult.UNKNOWN_ERROR;
        }
        return ProcessResult.createError(executeResult.getExecuteStatus());
    }
}
