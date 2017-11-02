package com.xiazki.service;

import com.facebook.swift.service.ThriftMethod;
import com.facebook.swift.service.ThriftService;

/**
 * Created by xiang.
 */
@ThriftService
public interface ITestService {
    @ThriftMethod
    void sayHello();
}
