package com.dora.httpframework.exception;

/**
 * @Author: Dora
 * @Description: 全局异常捕获
 * @Date: 2021/12/13
 */
public class DoraException extends RuntimeException{
    public DoraException() {
        super();
    }

    public DoraException(String message) {
        super(message);
    }

    public DoraException(String message, Object...args) {
        super(String.format(message, args));
    }

    public DoraException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoraException(Throwable cause) {
        super(cause);
    }

    protected DoraException(String message, Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
