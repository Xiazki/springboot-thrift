package com.xiazki.thrift.client.configure;

import com.google.common.net.HostAndPort;
import io.airlift.units.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author xiang.
 * @date 2017/11/23
 */
@Data
@ConfigurationProperties(prefix = "thrift.client")
public class ThriftClientProperties {

    /**
     * 客户端 ip 端口号配置
     */
    private HashMap<String,String> config;
    private String zkServerList;
    int poolMaxTotal = 512;
    int poolMaxTotalPerKey = 8;
    int poolMaxIdlePerKey = 6;
    int poolMinIdlePerKey = 2;
    long poolMaxWait = 1000;
    long timeBetweenEvictionRunsMillis = 10;
    boolean blockWhenExhausted = true;
    boolean lifo = false;

    Duration connectTimeout = new Duration(1, TimeUnit.SECONDS);
    Duration receiveTimeout = new Duration(1, TimeUnit.SECONDS);
    Duration readTimeout = new Duration(1, TimeUnit.SECONDS);
    Duration writeTimeout = new Duration(1, TimeUnit.SECONDS);
    int maxFrameSize = 16777216;
    HostAndPort socksProxy;


}
