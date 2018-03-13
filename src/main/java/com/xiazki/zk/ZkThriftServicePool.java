package com.xiazki.zk;

import com.google.common.collect.Lists;
import com.xiazki.exception.ServerNotFoundException;
import com.xiazki.zk.ip.ServerIpHostData;
import org.I0Itec.zkclient.ZkClient;
import org.apache.helix.util.ZKClientPool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiang.
 * @date 2018/3/7
 */
public class ZkThriftServicePool {

    private String serviceName;
    private String zkServerList;
    private Map<String, List<ServerIpHostData>> map = new ConcurrentHashMap<>();

    private void initSerIpHostDataMap() {
        List<String> childList = Lists.newArrayList();
        String servicePath = "/" + serviceName;
        ZkClient zkClient = ZKClientPool.getZkClient(zkServerList);
        if (!zkClient.exists(servicePath)) {
            throw new ServerNotFoundException("Service " + serviceName + " not found.");
        }
        childList = zkClient.getChildren(servicePath);
        childList.forEach(path -> {
        });
    }
}
