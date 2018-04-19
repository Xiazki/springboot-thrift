package com.xiazki.thrift.server;

import com.facebook.nifty.core.RequestContext;
import com.facebook.swift.service.ThriftEventHandler;
import com.xiazki.zk.balance.BalanceOperator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiang.
 * @date 2018/3/7
 */
@Data
@Slf4j
public class ZkServerBalanceHandler extends ThriftEventHandler {

    private BalanceOperator balanceOperator;

    public ZkServerBalanceHandler(BalanceOperator balanceOperator) {
        this.balanceOperator = balanceOperator;
    }

    @Override
    public Object getContext(String methodName, RequestContext requestContext) {
        balanceOperator.increment(1);
        return super.getContext(methodName, requestContext);
    }

    @Override
    public void done(Object context, String methodName) {
        super.done(context, methodName);
        balanceOperator.increment(-1);
    }
}
