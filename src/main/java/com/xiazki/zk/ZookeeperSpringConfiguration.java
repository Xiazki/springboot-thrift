package com.xiazki.zk;

import com.xiazki.thrift.server.configure.ThriftServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiang.
 * @date 2018/3/6
 */
@Configuration
@EnableConfigurationProperties(value = ThriftServerProperties.class)
public class ZookeeperSpringConfiguration {

}
