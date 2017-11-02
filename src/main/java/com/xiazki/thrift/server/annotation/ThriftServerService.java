package com.xiazki.thrift.server.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author xiang.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface ThriftServerService {
}
