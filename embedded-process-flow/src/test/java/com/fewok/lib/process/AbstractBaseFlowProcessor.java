package com.fewok.lib.process;

import com.fewok.lib.process.container.ExecuteResult;
import com.fewok.lib.process.entity.CommonInput;
import com.fewok.lib.process.entity.CommonOutput;
import com.fewok.lib.process.processor.BaseFlowProcessor;
import com.fewok.lib.process.util.JsonProcess;
import lombok.extern.slf4j.Slf4j;

/**
 * 道流程
 *
 * @author notreami on 18/7/7.
 */
@Slf4j
public abstract class AbstractBaseFlowProcessor<I, O> extends BaseFlowProcessor<CommonInput<I>, CommonOutput<O>> {

    @Override
    protected void preProcess(CommonInput<I> input) {
        log.info("preProcess:{}", JsonProcess.toJSONString(input));
    }

    @Override
    protected ExecuteResult checkInput(CommonInput<I> input) {
        return ExecuteResult.OK;
    }

    @Override
    protected CommonOutput<O> handleCheckFail(CommonInput<I> input, ExecuteResult executeResult) {
        return CommonOutput.PARAM_ERROR;
    }

    @Override
    protected CommonOutput<O> handleException(CommonInput<I> input, CommonOutput<O> output, Exception e) {
        String msg = String.format("Process[%s]处理异常,commonInput=%s", getProcessorName(), JsonProcess.toJSONString(input));
        log.error(msg, e);
        return CommonOutput.UNKNOWN_ERROR;
    }
}
