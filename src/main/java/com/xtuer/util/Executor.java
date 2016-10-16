package com.xtuer.util;

/**
 * @see com.xtuer.util.RedisUtils
 *
 * Created by SUNX on 2016/10/16.
 */
public interface Executor<T> {
    public T execute();
}
