package com.fewok.lib.process.container;

import com.google.common.collect.ImmutableMap;
import com.fewok.lib.process.ProcessExecutor;
import com.fewok.lib.process.exception.ProcessException;
import com.fewok.lib.process.execute.executor.AsyncProcessExecutor;
import com.fewok.lib.process.execute.executor.ParallelProcessExecutor;
import com.fewok.lib.process.execute.executor.SyncProcessExecutor;
import com.fewok.lib.process.processor.BaseActivityProcessor;
import com.fewok.lib.process.type.InvokeType;
import com.fewok.lib.process.type.ProcessExceptionType;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 构建流程执行上下文
 *
 * @author notreami on 18/7/7.
 */
public class ExecuteContextBuilder {

    private ExecuteContext executeContext = new ExecuteContext();

    /**
     * 增加一个活动流程
     *
     * @param activityProcessor 一个活动流程
     * @return ExecuteContextBuilder
     */
    public ExecuteContextBuilder then(@NonNull BaseActivityProcessor activityProcessor, @NonNull ExecuteRule executeRule) {
        ExecuteContext.ExecuteContainer executeContainer = executeContext.getExecuteContainer();
        if (executeContainer == null) {
            executeContainer = ExecuteContext.ExecuteContainer.builder().build();
            executeContext.setExecuteContainer(executeContainer);
        }

        addExecuteInfo(activityProcessor, executeRule, executeContainer);
        return this;
    }

    /**
     * 增加多个活动流程
     *
     * @param activityProcessors 多个活动流程
     * @return ExecuteContextBuilder
     */
    public ExecuteContextBuilder all(@NonNull BaseActivityProcessor... activityProcessors) {
        for (BaseActivityProcessor activityProcessor : activityProcessors) {
            if (activityProcessor == null) {
                throw new ProcessException(ProcessExceptionType.EXECUTE_CONTEXT_BUILD_EXCEPTION);
            }
            then(activityProcessor, ExecuteRule.builder().build());
        }
        return this;
    }

    /**
     * 增加恢复流程（用于恢复当前活动流程）
     *
     * @param activityProcessor 恢复流程
     * @return ExecuteContextBuilder
     */
    public ExecuteContextBuilder revise(@NonNull BaseActivityProcessor activityProcessor, @NonNull ExecuteRule executeRule) {
        ExecuteContext.ExecuteContainer executeContainer = getCurrentExecuteContainer();

        addExecuteInfo(activityProcessor, executeRule, executeContainer);
        return this;
    }

    /**
     * 增加恢复流程（用于恢复当前活动流程）
     *
     * @param activityProcessors 恢复流程
     * @return ExecuteContextBuilder
     */
    public ExecuteContextBuilder reviseMore(@NonNull BaseActivityProcessor... activityProcessors) {
        ExecuteContext.ExecuteContainer executeContainer = getCurrentExecuteContainer();

        for (BaseActivityProcessor activityProcessor : activityProcessors) {
            if (activityProcessor == null) {
                throw new ProcessException(ProcessExceptionType.EXECUTE_CONTEXT_BUILD_EXCEPTION);
            }
            addExecuteInfo(activityProcessor, ExecuteRule.builder().build(), executeContainer);
        }
        return this;
    }

    /**
     * 合并之前增加恢复流程，作为当前活动流程的恢复流程
     *
     * @return ExecuteContextBuilder
     */
    public ExecuteContextBuilder mergeBeforeRevise() {
        ExecuteContext.ExecuteContainer executeContainer = getCurrentExecuteContainer();

        Integer subscript = executeContext.getReviseExecuteContainerMap().size() - 1;
        Map<Integer, ExecuteContext.ExecuteContainer> reviseExecuteContainerMap = executeContext.getReviseExecuteContainerMap();
        for (int i = 0; i < subscript; i++) {
            ExecuteContext.ExecuteContainer temp = reviseExecuteContainerMap.get(i);
            if (temp == null) {
                continue;
            }
            if (CollectionUtils.isEmpty(temp.getExecuteInfoList())) {
                continue;
            }
            temp.getExecuteInfoList().forEach(executeInfo -> {
                addExecuteInfo(executeInfo.getActivityProcessor(), executeInfo.getExecuteRule(), executeContainer);
            });
        }
        return this;
    }

    public ExecuteContextBuilder fromExecutor(@NonNull ExecutorService executorService) {
        if (executorService == null) {
            throw new ProcessException(ProcessExceptionType.EXECUTE_CONTEXT_BUILD_EXCEPTION);
        }
        Map<InvokeType, ProcessExecutor> processExecutorMap = ImmutableMap.<InvokeType, ProcessExecutor>builder()
                .put(InvokeType.SYNC, new SyncProcessExecutor<>(executorService))
                .put(InvokeType.ASYNC, new AsyncProcessExecutor<>(executorService))
                .put(InvokeType.PARALLEL, new ParallelProcessExecutor<>(executorService))
                .build();

        executeContext.setProcessExecutorMap(processExecutorMap);
        return this;
    }

    /**
     * 生成
     *
     * @return ExecuteContext
     */
    public ExecuteContext build() {
        if (checkExecuteContext()) {
            return executeContext;
        }
        throw new ProcessException(ProcessExceptionType.EXECUTE_CONTEXT_BUILD_EXCEPTION);
    }


    private void addExecuteInfo(@NonNull BaseActivityProcessor activityProcessor, @NonNull ExecuteRule executeRule, @NonNull ExecuteContext.ExecuteContainer executeContainer) {
        List<ExecuteContext.ExecuteInfo> executeInfoList = executeContainer.getExecuteInfoList();
        if (executeInfoList == null) {
            executeInfoList = new ArrayList<>();
            executeContainer.setExecuteInfoList(executeInfoList);
        }
        int retryCount = executeRule.getRetryCount();
        int executeTotal = retryCount <= 0 ? 1 : retryCount + 1;

        ExecuteContext.ExecuteInfo executeInfo = ExecuteContext.ExecuteInfo.builder()
                .activityProcessor(activityProcessor)
                .executeRule(executeRule)
                .executeTotal(executeTotal)
                .build();

        executeInfoList.add(executeInfo);
    }

    private ExecuteContext.ExecuteContainer getCurrentExecuteContainer() {
        ExecuteContext.ExecuteContainer executeContainer = executeContext.getExecuteContainer();
        if (executeContainer == null || CollectionUtils.isEmpty(executeContainer.getExecuteInfoList())) {
            throw new ProcessException(ProcessExceptionType.EXECUTE_CONTEXT_BUILD_EXCEPTION);
        }

        Integer subscript = executeContainer.getExecuteInfoList().size() - 1;
        Map<Integer, ExecuteContext.ExecuteContainer> reviseExecuteContainerMap = executeContext.getReviseExecuteContainerMap();
        if (MapUtils.isEmpty(reviseExecuteContainerMap)) {
            reviseExecuteContainerMap = new HashMap<>();
            executeContext.setReviseExecuteContainerMap(reviseExecuteContainerMap);
        }
        return reviseExecuteContainerMap.computeIfAbsent(subscript, k -> ExecuteContext.ExecuteContainer.builder().build());
    }

    private boolean checkExecuteContext() {
        return executeContext.getExecuteContainer() != null && MapUtils.isNotEmpty(executeContext.getProcessExecutorMap());
    }
}
