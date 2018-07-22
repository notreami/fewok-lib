package com.fewok.lib.process;

/**
 * 流程定义
 *
 * @param <I> 输入
 * @param <O> 输出
 * @author notreami on 18/7/7.
 */
@FunctionalInterface
public interface Processor<I, O> {

    /**
     * 流程
     *
     * @param input 输入
     * @return 输出
     */
    O process(I input);

    /**
     * 流程名称
     *
     * @return 流程名称
     */
    default String getProcessorName() {
        return this.getClass().getSimpleName();
    }
}
