package com.xiazki.thrift.client.annotation;

import java.lang.annotation.*;

/**
 * 在Bean字段上使用该注解注册远程服务代理类.
 *
 * @author xiang.
 * @date 2017/11/23
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThriftClient {

}
