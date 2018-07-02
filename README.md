
[![Build Status](https://travis-ci.org/ppdai-incubator/raptor.svg?branch=master)](https://travis-ci.org/ppdai-incubator/raptor)
[![Coverage Status](https://coveralls.io/repos/github/ppdai-incubator/raptor/badge.svg)](https://coveralls.io/github/ppdai-incubator/raptor)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# ![](docs/assets/logo.png)Raptor 拍拍贷微服务rpc组件

Raptor微服务rpc组件是拍拍贷基础框架部参考、借鉴了大量已有rpc框架、rpc组件的设计，研发的一款基于google protobuf的轻量级，可扩展的rpc组件。

1. 契约驱动(Contract-First)开发模式，采用protobuf契约，自动生成服务器端接口和客户端代码
2. 基于HTTP协议，一套组件同时覆盖内部服务开发和对外开放场景
3. RPC/REST混合模式，既可以使用客户端以RPC/HTTP/JSON方式调用，也可以通过浏览器以REST/HTTP/JSON方式调用
4. 支持多种强类型客户端自动生成，Java/C#/Python/iOS/Android...
5. 设计实现简单轻量，依赖少，可以和Spring(Boot)无缝集成

**详细参考文档请参考 [wiki](https://github.com/ppdai-incubator/raptor/wiki)**

# 拍拍贷微服务体系

拍拍贷微服务体系是拍拍贷基础框架部总结内部微服务多年实践，参考、吸收大量业内解决方案形成的适合中型互联网公司的微服务解决方案。

拍拍贷微服务体系主要组成部分：
- Raptor rpc组件。
- Radar服务注册中心。
- Kong网关。

拍拍贷微服务体系的总体调用关系图：

![](docs/assets/microservice.png)

微服务实例启动之后，会自动注册到radar服务注册中心，实例启动正常后，kong网关周期性的将实例信息同步到kong的插件配置。
微服务之间的调用、zuul网关调用微服务，都是通过域名进行调用，域名解析到kong网关。Kong网关根据域名和微服务的对应关系，对微服务实例进行负载均衡运算后，得到一个实例，最终进行调用。

拍拍贷微服务体系主要架构考虑：
- 由Kong网关形成的集中式服务治理。降低由于客户端服务治理bugfix等引起的升级成本。
- 采用HTTP 1.1作为底层传输协议，从外部到内部无需进行协议转换。
- 采用HTTP 1.1作为底层传输协议，不会引起原有基于HTTP协议的已有设施失效。
