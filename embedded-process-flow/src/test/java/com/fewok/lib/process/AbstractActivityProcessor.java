package com.fewok.lib.process;

import com.fewok.lib.process.entity.CommonInput;
import com.fewok.lib.process.entity.CommonOutput;
import com.fewok.lib.process.entity.PContext;
import com.fewok.lib.process.processor.BaseActivityProcessor;
import com.fewok.lib.process.entity.*;

/**
 * @author notreami on 18/7/7.
 */
public abstract class AbstractActivityProcessor<I, H, O> extends BaseActivityProcessor<PContext<CommonInput<I>, H, CommonOutput<O>>> {
}
