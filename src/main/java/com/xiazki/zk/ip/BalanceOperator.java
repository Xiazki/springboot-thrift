package com.xiazki.zk.ip;

/**
 * @author xiang.
 * @date 2018/3/7
 */
public interface BalanceOperator {

    /**
     * 负载因子增量
     *
     * @param value value
     * @return 成功返回true
     */
    boolean increment(Integer value);

}
