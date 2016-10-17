package com.xtuer.bean;

/**
 * Rest通用响应格式
 *
 * @param <T> 业务数据
 */
public class Result<T> {
    private boolean success;
    private String message;
    private T data;

    public Result(boolean success, String message) {
        this(success, message, null);
    }

    public Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public Result setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result setData(T data) {
        this.data = data;
        return this;
    }

    public static Result ok() {
        return new Result(true, "成功");
    }

    public static Result error() {
        return new Result(false, "错误");
    }

}
