package com.xiazki.thrift.client.configure;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiang.
 * @date 2017/11/23
 */

@Configuration
@EnableConfigurationProperties(value = ThriftClientProperties.class)
@AutoConfigureOrder
public class ThriftClientSpringConfiguration {


}
