package com.fewok.lib.process.entity;

import com.fewok.lib.process.container.ProcessContext;

/**
 * @author notreami on 18/7/7.
 */
public class PContext<I, H, O> extends ProcessContext<I, H, O> {
    public PContext(I commonInput, H holder, O commonOutput) {
        super(commonInput, holder, commonOutput);
    }
}
