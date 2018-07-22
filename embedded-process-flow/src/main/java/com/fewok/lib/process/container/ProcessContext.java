package com.fewok.lib.process.container;

import com.fewok.lib.process.util.IdGenerator;
import com.fewok.lib.process.util.IdWorker;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程上下文
 *
 * @author notreami on 18/7/7.
 */
public class ProcessContext<I, H, O> {

    //用于 IdWorker 初始化
    private static final IdGenerator ID_GENERATOR = new IdGenerator();

    /**
     * 入参
     */
    @Getter
    private I commonInput;

    /**
     * 持有数据
     */
    @Getter
    @Setter
    private H holder;

    /**
     * 出参
     */
    @Getter
    private O commonOutput;

    @Getter
    private Long processId = IdWorker.getIdWorker().nextId();

    public ProcessContext(I commonInput, H holder, O commonOutput) {
        this.commonInput = commonInput;
        this.holder = holder;
        this.commonOutput = commonOutput;
    }
}
