package com.xiazki.zk.ip;

import lombok.Data;

/**
 * @author xiang.
 * @date 2018/3/7
 * <p>
 * 服务Ip及其Host 以及负载计算因子
 */
@Data
public class ServerIpHostData implements Comparable<ServerIpHostData> {

    private String ip;

    private Integer port = 0;

    private Integer balance = 0;

    public ServerIpHostData() {
    }

    public ServerIpHostData(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public int compareTo(ServerIpHostData o) {
        if (o == null) {
            return 1;
        }
        return this.getBalance().compareTo(o.getBalance());
    }
}
