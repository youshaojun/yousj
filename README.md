# 用于快速构建Java项目而整合的一些常用模块

### 项目结构
<details>
<summary>展开查看</summary>
<pre><code>.
├─yousj-all
├─yousj-bom
├─yousj-core 常用工具
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─core
│          │              ├─constant
│          │              ├─entity
│          │              ├─enums
│          │              ├─exception
│          │              └─utils
│          └─resources
│              └─META-INF
├─yousj-crypto 加解密
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─crypto
│          │              ├─advice
│          │              ├─annotation
│          │              ├─constant
│          │              ├─handler
│          │              ├─properties
│          │              └─utils
│          └─resources
│              └─META-INF
├─yousj-datasource 数据源
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─datasource
│          │              ├─config
│          │              ├─convert
│          │              ├─entity
│          │              └─interceptor
│          └─resources
│              └─META-INF
├─yousj-excel easyexcel
│  └─src
│      └─main
│          └─java
│              └─top
│                  └─yousj
│                      └─excel
│                          ├─handler
│                          ├─strategy
│                          └─utils
├─yousj-exception 异常处理
│  └─src
│      └─main
│          └─java
│              └─top
│                  └─yousj
│                      └─exception
├─yousj-log 日志
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─log
│          │              ├─aop
│          │              ├─constant
│          │              ├─logback
│          │              └─properties
│          └─resources
│              └─META-INF
├─yousj-redis 缓存
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─redis
│          │              ├─annotation
│          │              ├─cache
│          │              ├─constant
│          │              ├─multi
│          │              ├─properties
│          │              └─utils
│          └─resources
│              └─META-INF
├─yousj-reload 热部署
│  ├─yousj-reload-class 热部署class[参考](https://gitee.com/huoyo/ko-time)
│  │  └─src
│  │      └─main
│  │          ├─java
│  │          │  └─top
│  │          │      └─yousj
│  │          │          └─reload
│  │          │              ├─controller
│  │          │              └─service
│  │          └─resources
│  │              └─retrans
│  │                  └─META-INF
│  │                      └─maven
│  │                          └─cn.langpy
│  │                              └─ko-time-retrans
│  └─yousj-reload-mapper 热部署mapper[待验证]
│      └─src
│          └─main
│              └─java
│                  └─top
│                      └─yousj
│                          └─reload
│                              ├─controller
│                              └─service
├─yousj-security 认证鉴权
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─security
│          │              ├─annotation
│          │              ├─config
│          │              ├─constant
│          │              ├─exception
│          │              ├─filter
│          │              ├─handler
│          │              ├─matcher
│          │              ├─properties
│          │              └─utils
│          └─resources
│              └─META-INF
├─yousj-swagger swagger文档
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─swagger
│          │              ├─config
│          │              ├─constant
│          │              ├─entity
│          │              └─spring
│          └─resources
│              └─META-INF
├─yousj-uaa 统一认证服务
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─top
│      │  │      └─yousj
│      │  │          └─uaa
│      │  │              ├─controller
│      │  │              ├─entity
│      │  │              │  ├─po
│      │  │              │  └─vo
│      │  │              │      └─request
│      │  │              ├─enums
│      │  │              ├─handler
│      │  │              ├─mapper
│      │  │              └─service
│      │  │                  └─impl
│      │  └─resources
│      │      └─mapper
│      └─test
│          └─java
│              └─top
│                  └─yousj
│                      └─uaa
└─yousj-web
    └─src
        └─main
            ├─java
            │  └─top
            │      └─yousj
            │          └─web
            │              ├─annotation
            │              ├─config
            │              └─date
            └─resources
                └─META-INF
</code></pre>
</details>
