# Dubbo源码阅读

## 使用方式简介

使用方式1：xml配置

一般分为\*Provider.xml与\*Consumer.xml

使用方式2：Spring boot应用中搭配spring-boot-dubbo项目使用注解实现

## 框架原理

结合阅读源码之前我自己之前的疑惑来逐步展开：

1. 为什么dubbo框架仅通过一些自定义标签配置，就可以在不做任何显式操作的情况下，随着spring容器而完成自身服务的启动？

    原理：dubbo通过spring开放的自定义标签扩展机制(需要对这个有点了解，在spring项目中通过DefaultNamespaceHandlerResolver实现，具体表现为以SPI形式加载META-INF/spring.handler下对应配置的类实现扩展)，
在dubbo内部通过`DubboNamespaceHandler`实现；其中扩展标签`<dubbo:service xxx/>`对应的实现类为`ServiceBean`，它是一个标准的Spring bean且实现了三个关键的接口，
分别为：`ApplicationContextAware`、`InitializingBean`以及`ApplicationListener`，在Spring容器实现中这三个接口都存在"回调"机制，
dubbo中通过`onApplicationEvent`方法中的**export**实现自身服务的启动。

2. dubbo为什么能通过标签中的`name`属性搭配各种服务实现组合，如不同的服务注册中心、通信协议等？

    原理：dubbo被设计为**微内核+扩展插件**的形式(开发者手册的官方说法)，其实为加载中心+SPI扩展点实现，通过`ExtensionLoader`去加载**META-INF/dubbo、META-INF/dubbo/internal以及META-INF/services**下的扩展点配置类
    (在`ServiceBean`的父类`ServiceConfig`中有详细逻辑)，通过标签配置name项去获取指定服务，并且为不同的服务设置有默认实现。
    
3. dubbo框架工作原理

    有了上面两条的解释，深入跟踪每一层(一个服务)的实现，如Registry、Protocol、Transpoter等，可以很清楚的知道服务机理与框架脉络。
    原理：duboo随着Spring容器的启动，去加载对应的配置并且选择不同的实现，dubbo服务"发布/订阅"通过特殊实现的**URL**对象"协调"，下面以最常用的dubbo协议与zk注册中心来梳理；随着服务的启动，
    dubbo会将能唯一标志服务实现的URL注册到zk，并且通过Netty启动TCP服务(有监控服务与RPC服务)，客户端调用服务时通过zk获取标志服务的URL(包含接口、服务器地址等关键信息)，通过Netty客户端与服务器通信，
    服务提供者将目标实现类序列化后以二进制格式返回到客户端，客户端获取结果反序列化后通过字节码+动态代理生成目标接口的动态实现用于客户端的透明化调用，消费者/客户端执行完成返回结果到服务器/提供者
    
4. 源码阅读指引

    源码有丰富的单元测试，先从provider开始，找一个服务提供者(如`ValidationProvider`)，debug模式运行main方法逐步跟踪即可。正常启动完成后，
    再找到对应的消费者以debug模式跟踪调试，最开始可能会对ExtensionLoader的嵌套调用很迷惑，多走两遍即可



# Apache Dubbo (incubating) Project

[![Build Status](https://travis-ci.org/apache/incubator-dubbo.svg?branch=master)](https://travis-ci.org/apache/incubator-dubbo) 
[![codecov](https://codecov.io/gh/apache/incubator-dubbo/branch/master/graph/badge.svg)](https://codecov.io/gh/apache/incubator-dubbo)
[![Gitter](https://badges.gitter.im/alibaba/dubbo.svg)](https://gitter.im/alibaba/dubbo?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
![license](https://img.shields.io/github/license/alibaba/dubbo.svg)
![maven](https://img.shields.io/maven-central/v/com.alibaba/dubbo.svg)

Apache Dubbo (incubating) is a high-performance, java based RPC framework open-sourced by Alibaba. Please visit [dubbo official site ](http://dubbo.incubator.apache.org) for quick start and documentations, as well as [Wiki](https://github.com/apache/incubator-dubbo/wiki) for news, FAQ, and release notes.

We are now collecting dubbo user info in order to help us to improve dubbo better, pls. kindly help us by providing yours on [issue#1012: Wanted: who's using dubbo](https://github.com/apache/incubator-dubbo/issues/1012), thanks :)

## Links

* [Side projects](https://github.com/apache/incubator-dubbo)
    * [Dubbo Spring Boot](https://github.com/apache/incubator-dubbo-spring-boot-project) - Spring Boot Project for Dubbo.
    * [Dubbo ops](https://github.com/apache/incubator-dubbo-ops) - The reference implementation for dubbo ops(dubbo-admin,dubbo-monitor-simple,dubbo-registry-simple,etc.).
    * [Dubbo website](https://github.com/apache/incubator-dubbo-website) - Apache Dubbo (incubating) documents
    * [Dubbo rpc-jsonrpc](https://github.com/apache/incubator-dubbo-rpc-jsonrpc) - The Json rpc module of Apache Dubbo (incubating) project
    * [Dubbo feature-test](https://github.com/apache/incubator-dubbo-feature-test) - Apache Dubbo (incubating) feature test
    * [Dubbo docs](https://github.com/apache/incubator-dubbo-docs) - Apache Dubbo (incubating) documentation  
* [Developer Mailing list](https://github.com/apache/incubator-dubbo/issues/1393) - Any questions or suggestions? [Subscribe](https://github.com/apache/incubator-dubbo/issues/1393) to (dev@dubbo.incubator.apache.org) to discuss with us.
* [Gitter channel](https://gitter.im/alibaba/dubbo) - Online chat room with Dubbo developers.
* [Dubbo user manual(English)](http://dubbo.apache.org/books/dubbo-user-book-en/) or [Dubbo用户手册(中文)](http://dubbo.apache.org/books/dubbo-user-book/) - Describe how to use Dubbo and all features of Dubbo concretely.
* [Dubbo developer guide(English)](http://dubbo.apache.org/books/dubbo-dev-book-en/) or [Dubbo开发手册(中文)](http://dubbo.apache.org/books/dubbo-dev-book/) - Detailly introduce the design principal, extension mechanisms, code conventions, version control and building project, etc.
* [Dubbo admin manual(English)](http://dubbo.apache.org/books/dubbo-admin-book-en/) or [Dubbo管理手册(中文)](http://dubbo.apache.org/books/dubbo-admin-book/) - Describe how to use Dubbo registry and admin-console.

