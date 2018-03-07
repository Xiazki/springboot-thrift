package com.xiazki.zk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiang.
 * @date 2018/3/6
 * <p>
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
     * 服务端口号
     */
    int servicePort;

    /**
     * zk 集群主机ip列表
     */
    String zkServerList;

}
