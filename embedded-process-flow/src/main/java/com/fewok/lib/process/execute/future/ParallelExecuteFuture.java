package com.fewok.lib.process.execute.future;

import com.fewok.lib.process.Processor;
import com.fewok.lib.process.container.ExecuteResult;
import com.fewok.lib.process.container.ProcessContext;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 并行执行
 *
 * @author notreami on 18/7/7.
 */
@AllArgsConstructor
public class ParallelExecuteFuture<R extends ExecuteResult> implements Future<R> {

    private R executeResult;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public R get() throws InterruptedException, ExecutionException {
        return executeResult;
    }

    @Override
    public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return executeResult;
    }

    @AllArgsConstructor
    public static class ExecuteRunnable<P extends ProcessContext, R extends ExecuteResult> implements Runnable {

        private Processor<P, R> processor;
        private P processContext;

        @Override
        public void run() {
            processor.process(processContext);
        }
    }
}
