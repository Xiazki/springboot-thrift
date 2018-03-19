package com.xiazki.zk.ip;

import java.util.List;

/**
 * @author xiang.
 * @date 2018/3/19
 * <p/>
 * 获取负载算法接口
 */
public interface BalanceProvider {

     ServerIpHostData getBalanceItem(List<ServerIpHostData> serverIpHostDataList);

}
