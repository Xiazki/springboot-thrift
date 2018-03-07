package com.xiazki.thrift.server.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiang.
 * thrift服务端配置类
 */
@Data
@ConfigurationProperties(prefix = "thrift.server")
public class ThriftServerProperties {

    /**
     * 暴露服务端口号
     */
    private int port = 8890;

    /**
     * 工作线程数量
     */
    private int workThreads = 4 * Runtime.getRuntime().availableProcessors();

    /**
     * 设置在等待队列上的请求的最大数量
     */
    private int workerQueueCapacity = 1024;

    private int acceptorThreadCount = 2;

    private int ioThreads = 2 * Runtime.getRuntime().availableProcessors();

    /**
     * 设置客户端请求的超时时间
     */
    private String idelTimeout = "5s";

}
