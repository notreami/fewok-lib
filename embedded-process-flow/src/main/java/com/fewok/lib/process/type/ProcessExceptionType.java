package com.fewok.lib.process.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程异常类型
 *
 * @author notreami on 18/7/7.
 */
@Getter
@AllArgsConstructor
public enum ProcessExceptionType {

    /**
     * ExecuteContext build异常
     */
    EXECUTE_CONTEXT_BUILD_EXCEPTION("ExecuteContext build异常"),

    /**
     * ExecuteContext 获取异常
     */
    EXECUTE_CONTEXT_EXCEPTION("ExecuteContext 获取异常"),

    /**
     * 流程执行 异常
     */
    PROCESS_EXECUTOR_EXCEPTION("流程执行 异常");

    private String message;
}
