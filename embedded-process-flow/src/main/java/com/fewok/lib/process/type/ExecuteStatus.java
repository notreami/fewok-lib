package com.fewok.lib.process.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程执行状态
 *
 * @author notreami on 18/7/7.
 */
@Getter
@AllArgsConstructor
public enum ExecuteStatus {

    OK(200, "执行成功"),
    SKIP(260, "执行跳过"),
    PARAM_ERROR(400, "参数有误"),
    UNKNOWN_ERROR(500, "未知的流程错误"),
    TIMEOUT_ERROR(504, "执行超时");


    private int code;
    private String message;
}
