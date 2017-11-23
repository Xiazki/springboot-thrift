package com.xiazki.thrift.client;

import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.google.common.net.HostAndPort;
import com.xiazki.thrift.client.annotation.ThriftClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 客户端服务类代理工厂
 *
 * @author xiang.
 * @date 2017/11/23
 */
@Data
@Slf4j
public class BeanProxyFactory<T> implements FactoryBean<T>, InitializingBean {

    private Class<T> clientClass;
    private ThriftClient thriftClient;
    private String host;
    private int port;
    private Boolean isSingleton = true;
    private volatile T value;
    private ThriftClientManager clientManager = new ThriftClientManager();
    private FramedClientConnector clientConnector;

    @Override
    public T getObject() throws Exception {
        return get();
    }

    @Override
    public Class<?> getObjectType() {
        return clientClass;
    }

    @Override
    public boolean isSingleton() {
        return isSingleton;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        clientConnector = new FramedClientConnector(HostAndPort.fromParts(host, port));
    }

    private T get() {
        if (value == null) {
            synchronized (this) {
                if (value == null) {
                    initValue();
                }
            }
        }
        return value;
    }

    private void initValue() {
        try {
            value = createCglibDynamicProxyClass();
        } catch (Exception e) {
            log.error("create proxy failed ", e);
        }
    }

    /**
     *
     * 生成代理类
     *
     * @return 客户端服务代理
     * @throws Exception
     */
    private T createCglibDynamicProxyClass() throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clientClass);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, methodProxy) -> {
            Object client = null;
            try {
                client = clientManager.createClient(clientConnector, clientClass).get();
                return methodProxy.invoke(client, args);
            } catch (Exception e) {
                log.error("clientClass = " + clientClass + "[Thrift service invoke exception]", e);
                throw e;
            } finally {
                if (client != null && client instanceof AutoCloseable) {
                    AutoCloseable closeable = (AutoCloseable) client;
                    closeable.close();
                }
            }
        });
        return (T) enhancer.create();
    }

    private T createJavassistDynamicProxyClass() throws Exception {
        return null;
    }

}
