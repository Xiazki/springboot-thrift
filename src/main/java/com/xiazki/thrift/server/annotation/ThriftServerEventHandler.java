package com.xiazki.thrift.server.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author xiang.
 * 用其来标注thrift是事件处理类，可用用MDC日志等
 * {@link com.facebook.swift.service.ThriftEventHandler} 继承于这个类都会注入ThriftProcessor
 * @see com.facebook.swift.service.ThriftServiceProcessor
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface ThriftServerEventHandler {

}
