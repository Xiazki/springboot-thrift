package com.xiazki.thrift.server;

import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.*;
import com.xiazki.thrift.server.annotation.ThriftServerImpl;
import com.xiazki.thrift.server.configure.ThriftServerProperties;
import com.xiazki.zk.ZkRegistThriftServiceFactory;
import com.xiazki.zk.balance.BalanceOperator;
import com.xiazki.zk.balance.OptimisticLockBalanceOperator;
import io.airlift.units.Duration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.type.StandardMethodMetadata;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author xiang.
 *         Thrift 服务启动类.
 */
@Slf4j
@Data
public class ThriftServerRunner implements CommandLineRunner, DisposableBean {

    @Autowired
    private AbstractApplicationContext context;
    @Autowired
    private ThriftServerProperties thriftServerProperties;
    @Autowired
    private ZkRegistThriftServiceFactory zkRegistThriftServiceFactory;
    private final List<Object> serviceList = new ArrayList<>();
    private List<ThriftEventHandler> thriftEventHandlers = new ArrayList<>();
    private ThriftServer thriftServer;
    private ThriftServerConfig thriftServerConfig = new ThriftServerConfig();

    /**
     * @throws Exception bean销毁的时候关闭thrift服务
     */
    @Override
    public void destroy() throws Exception {
        Optional.ofNullable(thriftServer).ifPresent(ThriftServer::close);
        log.info("Thrift Server closed.");
    }

    /**
     * 注册和启动Thrift服务
     *
     * @throws Exception SpringBoot 启动时加载
     */
    @Override
    public void run(String... strings) throws Exception {
        log.info("Starting Thrift Server ...");
        initServiceList();
        initThriftEventHandler();
        initThriftConfiguration();
        if (serviceList.isEmpty()) {
            log.info("No Thrift Service found in context.");
        } else {
            ThriftServiceProcessor thriftServiceProcessor = new ThriftServiceProcessor(new ThriftCodecManager(), thriftEventHandlers, serviceList);
            thriftServer = new ThriftServer(thriftServiceProcessor, thriftServerConfig);
            new Thread(() -> this.thriftServer.start()).start();
            log.info("Thrift Server Started");
        }
    }

    /**
     * 获取容器中注入的service
     */
    private void initServiceList() {
        if (serviceList.isEmpty()) {
            getBeanNamesFromContext(ThriftServerImpl.class, context.getBeanNamesForAnnotation(ThriftService.class))
                    .forEach(name -> {
                        Object service = context.getBean(name);
                        serviceList.add(service);
                        log.info("'{}' service has been registered.", service.getClass().getName());
                    });
        }
    }

    private void initThriftEventHandler() {
        //添加处理服务端负载均衡处理类
        BalanceOperator balanceOperator = new OptimisticLockBalanceOperator(zkRegistThriftServiceFactory.getServiceName(), zkRegistThriftServiceFactory.getZkClient());
        ZkServerBalanceHandler zkServerBalanceHandler = new ZkServerBalanceHandler(balanceOperator);
        thriftEventHandlers.add(zkServerBalanceHandler);
        //扫描容器
        //// TODO: 2018/3/7 从容器中找到EventHandler

    }

    private void initThriftConfiguration() {
        thriftServerConfig = new ThriftServerConfig();
        thriftServerConfig.setPort(thriftServerProperties.getPort());
        thriftServerConfig.setAcceptorThreadCount(thriftServerProperties.getAcceptorThreadCount());
        thriftServerConfig.setIoThreadCount(thriftServerProperties.getIoThreads());
        thriftServerConfig.setWorkerThreads(thriftServerProperties.getWorkThreads());
        thriftServerConfig.setMaxQueuedRequests(thriftServerProperties.getWorkerQueueCapacity());
        thriftServerConfig.setIdleConnectionTimeout(Duration.valueOf(thriftServerProperties.getIdelTimeout()));
    }

    private Stream<String> getBeanNamesFromContext(Class<? extends Annotation> annotationType, String[] beanNames) {
        return Stream.of(beanNames).filter(name -> {

            final BeanDefinition beanDefinition = context.getBeanFactory().getBeanDefinition(name);
            final Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(annotationType);

            if (!beansWithAnnotation.isEmpty()) {
                return beansWithAnnotation.containsKey(name);
            } else if (beanDefinition.getSource() instanceof StandardMethodMetadata) {
                StandardMethodMetadata metadata = (StandardMethodMetadata) beanDefinition.getSource();
                return metadata.isAnnotated(annotationType.getName());
            }

            return false;
        });
    }

}
