package com.fewok.lib.process.container;

import com.fewok.lib.process.type.ExecuteStatus;
import lombok.Builder;
import lombok.Data;

/**
 * 一个活动流程执行结果
 *
 * @author notreami on 18/7/7.
 */
@Data
@Builder
public class ExecuteResult {

    public static final ExecuteResult OK = createSuccess(ExecuteStatus.OK);
    public static final ExecuteResult SKIP = createSuccess(ExecuteStatus.SKIP);
    public static final ExecuteResult PARAM_ERROR = createError(ExecuteStatus.PARAM_ERROR, "参数有误");
    public static final ExecuteResult UNKNOWN_ERROR = createError(ExecuteStatus.UNKNOWN_ERROR);
    public static final ExecuteResult TIMEOUT_ERROR = createError(ExecuteStatus.TIMEOUT_ERROR);

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 状态信息
     */
    private ExecuteStatus executeStatus;

    /**
     * 异常栈
     */
    private Exception exception;

    /**
     * 错误信息
     */
    private String message;

    public static ExecuteResult createSuccess(ExecuteStatus executeStatus) {
        return ExecuteResult.builder().success(true).executeStatus(executeStatus).build();
    }

    public static ExecuteResult createError(ExecuteStatus executeStatus) {
        return ExecuteResult.builder().success(false).executeStatus(executeStatus).build();
    }

    public static ExecuteResult createError(ExecuteStatus executeStatus, Exception exception) {
        return ExecuteResult.builder().success(false).executeStatus(executeStatus).exception(exception).build();
    }

    public static ExecuteResult createError(ExecuteStatus executeStatus, String message) {
        return ExecuteResult.builder().success(false).executeStatus(executeStatus).message(message).build();
    }
}
