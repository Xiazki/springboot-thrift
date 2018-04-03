package com.xiazki.thrift.client.configure;

import com.xiazki.thrift.client.ThriftClientBeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiang.
 * @date 2017/11/23
 * 移动
 */

@Configuration
@EnableConfigurationProperties(value = ThriftClientProperties.class)
@AutoConfigureOrder
public class ThriftClientSpringConfiguration {

    @Bean
    public ThriftClientBeanPostProcessor thriftClientBeanPostProcessor() {
        return new ThriftClientBeanPostProcessor();
    }
}
