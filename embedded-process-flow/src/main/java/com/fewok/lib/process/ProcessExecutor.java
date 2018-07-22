package com.fewok.lib.process;

import java.util.concurrent.Future;

/**
 * 流程执行器
 *
 * @param <I> 输入
 * @param <R> 执行结果
 * @author notreami on 18/7/7.
 */
@FunctionalInterface
public interface ProcessExecutor<I, R> {

    /**
     * 流程执行
     *
     * @param processor 流程
     * @param input     输入
     * @return 执行结果
     */
    Future<R> execute(Processor<I, R> processor, I input);
}
