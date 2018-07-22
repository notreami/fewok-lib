package com.fewok.lib.process.container;

import com.fewok.lib.process.type.InvokeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流程执行规则
 *
 * @author notreami on 18/7/7.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteRule {

    /**
     * 执行方式（同步、异步、并行）（默认 同步）
     */
    @Builder.Default
    private InvokeType invokeType = InvokeType.SYNC;

    /**
     * 等待之前的流程执行
     */
    @Builder.Default
    private boolean isWaitBeforeProcessor = false;

    /**
     * 重试次数（默认 0 次）
     */
    @Builder.Default
    private int retryCount = 0;

    /**
     * 每个流程执行的超时时间（默认 1 s）
     */
    @Builder.Default
    private int timeoutMillis = 1000;
}
