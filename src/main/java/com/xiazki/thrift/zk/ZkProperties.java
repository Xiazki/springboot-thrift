package com.xiazki.thrift.zk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiang.
 * @date 2018/3/6
 *
 * zk相关配置文件
 */
@Data
@ConfigurationProperties(prefix = "zk")
public class ZkProperties {

    /**
     * 服务名称
     */
    String serviceName;

    /**
     * zk 集群主机ip列表
     */
    String zkServerList;

}
