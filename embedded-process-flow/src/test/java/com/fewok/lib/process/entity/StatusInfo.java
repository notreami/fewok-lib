package com.fewok.lib.process.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author notreami on 18/7/7.
 */
@Getter
@AllArgsConstructor
public enum StatusInfo {

    OK(200, "执行成功"),
    SKIP(260, "执行跳过"),
    PARAM_ERROR(400, "参数有误"),
    UNKNOWN_ERROR(500, "未知的流程错误");


    private int code;
    private String message;
}
