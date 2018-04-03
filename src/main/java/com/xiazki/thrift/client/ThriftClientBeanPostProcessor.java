package com.xiazki.thrift.client;

import com.xiazki.thrift.client.annotation.ThriftClient;
import com.xiazki.thrift.client.configure.ThriftClientProperties;
import com.xiazki.zk.ZkThriftServerDataPool;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiang.
 * @date 2017/11/23
 * <p/>
 * 向Bean中注入Thrift Client
 */
@Data
@Slf4j
public class ThriftClientBeanPostProcessor implements DisposableBean, BeanPostProcessor, ApplicationContextAware {

    @Autowired
    private ThriftClientProperties thriftClientProperties;
    private ApplicationContext applicationContext;
    private ConcurrentHashMap<String, BeanProxyFactory<?>> proxyFactoryConcurrentHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ZkThriftServerDataPool> poolConcurrentHashMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        if (AopUtils.isAopProxy(bean)) {
            clazz = AopUtils.getTargetClass(bean);
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            ThriftClient thriftClient = field.getAnnotation(ThriftClient.class);
            if (this != null) {
                try {
                    //注入
                    field.set(bean, this.refer(thriftClient, field.getType()));
                    //// TODO: 2018/3/19 异常处理
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public void destroy() throws Exception {

    }

    private Object refer(ThriftClient thriftClient, Class<?> clientClass) {
        String name = thriftClient.serviceName();
        BeanProxyFactory beanProxyFactory = proxyFactoryConcurrentHashMap.get(name);
        if (beanProxyFactory == null) {
            BeanProxyFactory<?> factory = new BeanProxyFactory<>();
            factory.setClientClass(clientClass);
            factory.setThriftClient(thriftClient);

            ZkThriftServerDataPool zkThriftServerDataPool = poolConcurrentHashMap.get(name);
            if (zkThriftServerDataPool == null) {
                ZkThriftServerDataPool pool = new ZkThriftServerDataPool(name, thriftClientProperties.getZkServerList());
                poolConcurrentHashMap.put(name, pool);
                zkThriftServerDataPool = pool;
            }
            factory.setZkThriftServerDataPool(zkThriftServerDataPool);
            proxyFactoryConcurrentHashMap.put(name, factory);
            beanProxyFactory = factory;
        }
        try {
            return beanProxyFactory.getObject();
            //// TODO: 2018/3/19 异常处理 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
