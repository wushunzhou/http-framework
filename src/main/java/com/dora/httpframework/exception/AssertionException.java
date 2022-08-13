package com.dora.httpframework.exception;

/**
 * @Describe 断言类异常
 * @Author dora 1.0.1
 **/
public class AssertionException extends RuntimeException  {

    public AssertionException() {
        super();
    }

    public AssertionException(String message) {
        super(message);
    }

    public AssertionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssertionException(Throwable cause) {
        super(cause);
    }

    protected AssertionException(String message, Throwable cause,
                                 boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
