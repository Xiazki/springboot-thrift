package com.xiazki.service;

import com.xiazki.thrift.server.annotation.ThriftServerImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by xiang.
 */
@ThriftServerImpl
@Slf4j
public class TestServiceImpl implements ITestService {

    @Override
    public void sayHello() {
        System.out.println("hello,this is a test service!");
    }

}
