package com.fewok.lib.process.processor;

import com.fewok.lib.process.Processor;
import com.fewok.lib.process.container.ExecuteResult;
import com.fewok.lib.process.type.ExecuteStatus;
import com.fewok.lib.process.util.JsonProcess;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程基本执行过程
 *
 * @author notreami on 18/7/7.
 */
@Slf4j
public abstract class BaseProcessor<I, O> implements Processor<I, O> {

    /**
     * 流程基本执行过程
     *
     * @param input 输入
     * @return 输出
     */
    @Override
    public O process(I input) {
        O output = null;
        try {
            preProcess(input);
            ExecuteResult executeResult = checkInput(input);
            if (executeResult.getExecuteStatus() != ExecuteStatus.OK) {
                if (!executeResult.isSuccess()) {
                    log.error("Processor前置验证失败: processor={}, message={}, input={}", getProcessorName(), executeResult.getMessage(), JsonProcess.toJSONString(input));
                }
                return handleCheckFail(input, executeResult);
            }
            beforeProcess(input);
            output = doProcess(input);
            afterProcess(input, output);
            return output;
        } catch (Exception e) {
            return handleException(input, output, e);
        }
    }

    /**
     * 预处理
     *
     * @param input 输入
     */
    protected void preProcess(I input) {
        // do something preProcess
    }

    /**
     * 参数校验/活动流程规则判断
     *
     * @param input 输入
     * @return 校验结果
     */
    protected ExecuteResult checkInput(I input) {
        return ExecuteResult.OK;
    }

    /**
     * 参数校验失败处理
     *
     * @param executeResult 校验结果
     * @return 输出
     */
    protected abstract O handleCheckFail(I input, ExecuteResult executeResult);

    /**
     * 流程执行前
     *
     * @param input 输入
     */
    protected void beforeProcess(I input) {
        // do something beforeProcess
    }

    /**
     * 流程执行
     *
     * @param input 输入
     * @return 输出
     * @throws Exception 异常
     */
    protected abstract O doProcess(I input) throws Exception;

    /**
     * 流程执行后
     *
     * @param input  输入
     * @param output 输出
     */
    protected void afterProcess(I input, O output) {
        // do something afterProcess
    }

    /**
     * 流程执行异常处理
     *
     * @param input  输入
     * @param output 输出
     * @param e      异常
     * @return 最终输出
     */
    protected abstract O handleException(I input, O output, Exception e);
}
