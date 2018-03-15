# springboot-thrift
功能尚不完善，持续开发中
# 使用
核心注解 </br>
@ThriftClient </br>
   &emsp; serviceName -服务名称(zookeeper注册结点名称) </br>
   &emsp; 在容器的bean 中像使用@Autowired一样在相关Field上使用@ThriftClient，会在bean初始化后向该bean注入服务代理类。</br>
@ThriftServerImpl </br>
    &emsp;注册rpc服务实现类

