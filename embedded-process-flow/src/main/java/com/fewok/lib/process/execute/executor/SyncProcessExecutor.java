package com.fewok.lib.process.execute.executor;

import com.fewok.lib.process.ProcessExecutor;
import com.fewok.lib.process.Processor;
import com.fewok.lib.process.container.ExecuteResult;
import com.fewok.lib.process.container.ProcessContext;
import com.fewok.lib.process.execute.future.SyncExecuteFuture;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 同步流程执行器
 *
 * @author notreami on 18/7/7.
 */
@AllArgsConstructor
public class SyncProcessExecutor<P extends ProcessContext, R extends ExecuteResult> implements ProcessExecutor<P, R> {

    private ExecutorService executor;

    @Override
    public Future<R> execute(Processor<P, R> processor, P processContext) {
        return new SyncExecuteFuture<>(processor.process(processContext));
    }
}