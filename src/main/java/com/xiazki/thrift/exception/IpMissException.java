package com.xiazki.thrift.exception;

/**
 * @author xiang.
 * @date 2018/3/6
 */
public class IpMissException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Ip is null.";
    }
}
