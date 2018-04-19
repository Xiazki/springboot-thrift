package com.xiazki.zk;

import com.google.common.collect.Lists;
import com.xiazki.zk.balance.BalanceProvider;
import com.xiazki.zk.balance.ServerIpHostData;
import lombok.Data;
import org.I0Itec.zkclient.ZkClient;
import org.apache.helix.util.ZKClientPool;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiang.
 * @date 2018/3/7
 */
@Data
public class ZkThriftServerDataPool {

    private String serviceName;
    private String zkServerList;
    private Map<String, List<String>> map = new ConcurrentHashMap<>();
    private ZkClient zkClient;
    private BalanceProvider balanceProvider;

    public ZkThriftServerDataPool(String serviceName, String zkServerList) {
        this.serviceName = serviceName;
        this.zkServerList = zkServerList;
        this.zkClient = ZKClientPool.getZkClient(zkServerList);

        subscribeDataChange();

        if (balanceProvider == null) {
            balanceProvider = serverIpHostDataList -> {
                if (serverIpHostDataList.size() > 0) {
                    Collections.sort(serverIpHostDataList);
                    return serverIpHostDataList.get(0);
                } else {
                    return null;
                }
            };
        }
    }

    /**
     * 当数据发生变化时修改
     */
    private void subscribeDataChange() {
        String servicePath = "/" + serviceName;
        zkClient.subscribeChildChanges(serviceName, (parentPath, currentChilds) -> {
            map.put(serviceName, currentChilds);
        });
    }

    public ServerIpHostData getIpHostData(String serviceName) {
        List<String> items = map.get(serviceName);
        if (items == null || items.isEmpty()) {
            //抛出异常
            return null;
        }
        List<ServerIpHostData> serverIpHostDatas = Lists.newArrayList();
        for (String children : items) {
            ServerIpHostData serverIpHostData = zkClient.readData(serviceName + "/" + children);
            serverIpHostDatas.add(serverIpHostData);
        }
        return balanceProvider.getBalanceItem(serverIpHostDatas);
    }
}
