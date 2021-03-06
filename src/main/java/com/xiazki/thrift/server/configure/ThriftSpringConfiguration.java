package com.xiazki.thrift.server.configure;

import com.xiazki.thrift.server.ThriftServerRunner;
import com.xiazki.thrift.server.annotation.ThriftServerImpl;
import com.xiazki.zk.ZkProperties;
import com.xiazki.zk.ZkRegistThriftServiceFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author xiang.
 */
@Configuration
@EnableConfigurationProperties(value = {ThriftServerProperties.class, ZkProperties.class})
@AutoConfigureOrder
public class ThriftSpringConfiguration {

    @Bean
    @ConditionalOnBean(annotation = ThriftServerImpl.class)
    @Scope(value = "singleton")
    public ThriftServerRunner thriftServerRunner() {
        return new ThriftServerRunner();
    }

    @Bean
    @Scope("singleton")
    public ZkRegistThriftServiceFactory zkRegistThriftServiceProcessor() {
        return new ZkRegistThriftServiceFactory();
    }
}
