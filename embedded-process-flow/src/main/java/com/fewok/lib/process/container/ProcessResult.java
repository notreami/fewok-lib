package com.fewok.lib.process.container;

import com.fewok.lib.process.type.ExecuteStatus;
import lombok.Builder;
import lombok.Data;

/**
 * 一组流程执行结果
 *
 * @author notreami on 18/7/7.
 */
@Data
@Builder
public class ProcessResult {

    public static final ProcessResult OK = createSuccess(ExecuteStatus.OK);
    public static final ProcessResult SKIP = createSuccess(ExecuteStatus.SKIP);
    public static final ProcessResult UNKNOWN_ERROR = createError(ExecuteStatus.UNKNOWN_ERROR);

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 状态信息
     */
    private ExecuteStatus executeStatus;


    public static ProcessResult createSuccess(ExecuteStatus executeStatus) {
        return ProcessResult.builder().success(true).executeStatus(executeStatus).build();
    }

    public static ProcessResult createError(ExecuteStatus executeStatus) {
        return ProcessResult.builder().success(false).executeStatus(executeStatus).build();
    }
}
