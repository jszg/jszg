package com.xtuer.exception;

/**
 * Created by Administrator on 2017/6/21.
 */
public class ApplicationException extends RuntimeException{
    private String errorViewName = null;
    public ApplicationException(String message) {
        this(message, null);
    }
    public ApplicationException(String message, String errorViewName) {
        super(message);
        this.errorViewName = errorViewName;
    }
    public String getErrorViewName() {
        return errorViewName;
    }
}
