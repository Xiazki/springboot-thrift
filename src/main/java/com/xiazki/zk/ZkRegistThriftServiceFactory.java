package com.xiazki.zk;

import com.xiazki.exception.IpMissException;
import com.xiazki.zk.ip.IpProvider;
import com.xiazki.zk.ip.ServerIpHostData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.ZkSerializer;
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
public class ZkRegistThriftServiceFactory {

    @Autowired
    private ZkProperties zkProperties;
    private String serviceName;
    private String servicePath;
    private int servicePort;
    private String serverList;
    private IpProvider ipProvider;
    private ZkSerializer zkSerializer;
    private ZkClient zkClient;

    @PostConstruct
    public void init() {
        propertiesSet();
        zkClient = registerService();
    }

    private void propertiesSet() {
        if (zkProperties != null) {
            serviceName = zkProperties.getServiceName();
            serverList = zkProperties.getZkServerList();
            servicePort = zkProperties.getServicePort();
        }

        if (serviceName == null) {
            throw new RuntimeException("ServiceName value can not be null.");
        }
        if (serverList == null) {
            throw new RuntimeException("ServerList value can not be null.");
        }
        if (servicePort == 0) {
            throw new RuntimeException("Service port value can not be null.");
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
        String serviceRootPath = "/" + zkProperties.getServiceName();
        ZkClient zkClient = new ZkClient(serverList);
        if (!zkClient.exists(serviceRootPath)) {
            zkClient.createPersistent(serviceRootPath);
        }
        String ip = ipProvider.getServiceIp();
        if (ip == null) {
            throw new IpMissException();
        }
        String serviceInstance = System.nanoTime() + "-" + ip;
        ServerIpHostData serverIpHostData = new ServerIpHostData(ip, servicePort);
        //这里创建临时结点，服务器宕机后zk会清除结点
        //// TODO: 2018/3/6 需要再次注册吗？
        servicePath = serviceRootPath + "/" + serviceInstance;
        zkClient.createEphemeral(servicePath, serverIpHostData);
        log.info("Register service instance {} at port {}", serviceInstance, servicePort);
        return zkClient;
    }
}
