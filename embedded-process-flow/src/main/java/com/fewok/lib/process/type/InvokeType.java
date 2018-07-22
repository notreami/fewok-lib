package com.fewok.lib.process.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 执行方式
 *
 * @author notreami on 18/7/7.
 */
@Getter
@AllArgsConstructor
public enum InvokeType {

    /**
     * 同步:执行结果要merge
     */
    SYNC("同步"),

    /**
     * 异步:执行结果要merge
     */
    ASYNC("异步"),

    /**
     * 并行:不关心结果
     */
    PARALLEL("并行");

    private String message;

}
