package com.dora.httpframework.core.base;

/**
 * @Describe 统一响应
 * @Author dora 1.0.1
 **/
public class RepResult<T> {

    private int code;
    private String msg;
    private T data;

    public RepResult() {
    }

    public RepResult(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public RepResult(int code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }


    // 默认成功，10101成功，非10101失败
    public static <T> RepResult<T> success(T data) {
        return new RepResult<>(10101, "success", data);
    }


    public static <T> RepResult<T> validateFail(T data) {
        return new RepResult<>(10104, "参数校验错误", data);
    }


    public static <T> RepResult<T> mysqlFail(T data) {
        return new RepResult<>(10105, "MySQL更新失败", data);
    }

    public static <T> RepResult<T> notificationFail(T data) {
        return new RepResult<>(10106, "通知失败", data);
    }


}
