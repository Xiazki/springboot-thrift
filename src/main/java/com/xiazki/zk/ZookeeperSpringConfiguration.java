package com.xiazki.zk;

import com.xiazki.thrift.server.configure.ThriftServerProperties;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author xiang.
 * @date 2018/3/6
 */
@Configuration
@EnableConfigurationProperties(value = ThriftServerProperties.class)
@AutoConfigureOrder
public class ZookeeperSpringConfiguration {

}
