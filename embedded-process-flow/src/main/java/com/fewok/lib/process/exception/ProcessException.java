package com.fewok.lib.process.exception;

import com.fewok.lib.process.type.ProcessExceptionType;

/**
 * 流程异常
 *
 * @author notreami on 18/7/7.
 */
public class ProcessException extends RuntimeException {

    public ProcessException(ProcessExceptionType processExceptionType) {
        super(processExceptionType.getMessage());
    }
}
