package com.xiazki.thrift.zk;

import com.xiazki.thrift.exception.IpMissException;
import com.xiazki.thrift.zk.ip.IpProvider;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xiang.
 * @date 2018/3/6
 * <p>
 * 向zk注册Thrift Service 处理器
 */
@Data
@Slf4j
public class ZkRegistThriftServiceProcessor {

    @Autowired
    private ZkProperties zkProperties;
    private String serviceName;
    private String serverList;
    private IpProvider ipProvider;

    @PostConstruct
    public void init() {
        propertiesSet();
        registerService();
    }

    private void propertiesSet() {
        if (zkProperties != null) {
            serviceName = zkProperties.getServiceName();
            serverList = zkProperties.getZkServerList();
        }

        if (serviceName == null) {
            throw new RuntimeException("ServiceName value can not be null.");
        }
        if (serverList == null) {
            throw new RuntimeException("ServerList value can not be null.");
        }

        if (ipProvider == null) {
            ipProvider = new IpProvider() {
                @Override
                public String getServiceIp() {
                    try {
                        InetAddress inetAddress = InetAddress.getLocalHost();
                        return inetAddress.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }
    }

    private ZkClient registerService() {
        String servicePath = "/" + zkProperties.getServiceName();
        ZkClient zkClient = new ZkClient(serverList);
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
        }
        String ip = ipProvider.getServiceIp();
        if (ip == null) {
            throw new IpMissException();
        }
        String serviceInstance = System.nanoTime() + "-" + ip;
        //这里创建临时结点，服务器宕机后zk会清除结点
        //// TODO: 2018/3/6 zk会再次注册吗？
        zkClient.createEphemeral(servicePath + "/" + serviceInstance);
        log.info("Register service instance {}", serviceInstance);
        return zkClient;
    }
}
