package com.xiazki.zk.balance;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkBadVersionException;
import org.apache.zookeeper.data.Stat;

/**
 * @author xiang.
 * @date 2018/3/7
 * <p>
 * 乐观锁更新负载因子
 */
@Data
@Slf4j
public class OptimisticLockBalanceOperator implements BalanceOperator {

    private String servicePath;
    private ZkClient zkClient;

    public OptimisticLockBalanceOperator() {
    }

    public OptimisticLockBalanceOperator(String servicePath, ZkClient zkClient) {
        this.servicePath = servicePath;
        this.zkClient = zkClient;
    }

    @Override
    public boolean increment(Integer value) {
        Stat stat = new Stat();
        ServerIpHostData serverIpHostData;
        while (true) {
            try {
                serverIpHostData = zkClient.readData(servicePath, stat);
                serverIpHostData.setBalance(serverIpHostData.getBalance() + value);
                zkClient.writeData(servicePath, serverIpHostData, stat.getVersion());
                return true;
            } catch (ZkBadVersionException e) {
                //重试
            } catch (Exception e) {
                //未知原因
                log.error("increment balance error:" + e.getMessage());
                return false;
            }
        }
    }

}
