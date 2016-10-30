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

    public Result<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static Result<String> ok() {
        return new Result<String>(true, "成功");
    }

    public static <T> Result<T> ok(T data) {
        return new Result<T>(true, "成功", data);
    }

    public static <T> Result<T> error(T data) {
        return new Result (false, "错误", data);
    }

    public static Result<String> error() {
        return new Result (false, "错误");
    }
}
