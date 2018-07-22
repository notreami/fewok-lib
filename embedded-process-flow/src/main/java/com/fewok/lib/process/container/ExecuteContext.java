package com.fewok.lib.process.container;

import com.fewok.lib.process.exception.ProcessException;
import com.fewok.lib.process.processor.BaseActivityProcessor;
import com.fewok.lib.process.ProcessExecutor;
import com.fewok.lib.process.type.InvokeType;
import com.fewok.lib.process.type.ProcessExceptionType;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 流程执行上下文
 *
 * @author notreami on 18/7/7.
 */
@Data
public class ExecuteContext {

    private ExecuteContainer executeContainer;

    private Map<Integer, ExecuteContainer> reviseExecuteContainerMap;

    private Map<InvokeType, ProcessExecutor> processExecutorMap;

    @Data
    @Builder
    public static class ExecuteContainer {

        @Builder.Default
        private int beginIndex = 0;

        @Builder.Default
        private int endIndex = 0;

        @Builder.Default
        private boolean isOnlySync = true;

        @Builder.Default
        private boolean isAllSuccess = true;

        private List<ExecuteInfo> executeInfoList;
    }


    /**
     * 执行单元（记录活动流程执行情况）
     */
    @Data
    @Builder
    public static class ExecuteInfo {

        /**
         * 活动流程
         */
        private BaseActivityProcessor activityProcessor;

        /**
         * 流程规则
         */
        private ExecuteRule executeRule;

        /**
         * 执行次数
         */
        @Builder.Default
        private int executeCount = 0;

        /**
         * 执行总次数
         */
        @Builder.Default
        private int executeTotal = 0;

        /**
         *
         */
        private Future<ExecuteResult> future;

        /**
         * 执行结果集
         */
        private ExecuteResult executeResult;
    }

    ExecuteContext() {

    }

    public static ExecuteContextBuilder builder() {
        return new ExecuteContextBuilder();
    }

    public ExecuteContext copy(ExecuteContext from) {
        if (from == null) {
            throw new ProcessException(ProcessExceptionType.EXECUTE_CONTEXT_EXCEPTION);
        }

        ExecuteContainer executeContainer = copyExecuteContainer(from.getExecuteContainer());
        Map<Integer, ExecuteContainer> fromRevise = from.getReviseExecuteContainerMap();
        Map<Integer, ExecuteContainer> toRevise;
        if (MapUtils.isEmpty(fromRevise)) {
            toRevise = Collections.emptyMap();
        } else {
            toRevise = new HashMap<>(fromRevise.size());
            fromRevise.forEach((k, v) -> {
                toRevise.put(k, copyExecuteContainer(v));
            });
        }
        ExecuteContext to = new ExecuteContext();
        to.setExecuteContainer(executeContainer);
        to.setReviseExecuteContainerMap(toRevise);
        to.setProcessExecutorMap(from.getProcessExecutorMap());
        return to;
    }

    private ExecuteContainer copyExecuteContainer(ExecuteContainer executeContainer) {
        List<ExecuteInfo> executeInfoList = executeContainer.getExecuteInfoList().stream().map(x -> {
            ExecuteRule rule = x.getExecuteRule();
            ExecuteRule executeRule = ExecuteRule.builder()
                    .invokeType(rule.getInvokeType())
                    .isWaitBeforeProcessor(rule.isWaitBeforeProcessor())
                    .retryCount(rule.getRetryCount())
                    .timeoutMillis(rule.getTimeoutMillis())
                    .build();

            return ExecuteInfo.builder()
                    .activityProcessor(x.getActivityProcessor())
                    .executeRule(executeRule)
                    .executeTotal(x.getExecuteTotal())
                    .build();
        }).collect(Collectors.toList());

        return ExecuteContainer.builder().executeInfoList(executeInfoList).build();
    }
}
