package com.xiazki.exception;

/**
 * @author xiang.
 * @date 2018/3/7
 */
public class ServerNotFoundException extends RuntimeException {

    public ServerNotFoundException() {
    }

    public ServerNotFoundException(String msg) {
        super(msg);
    }
}
