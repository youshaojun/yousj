# 用于快速构建Java项目而整合的一些常用模块

### 项目结构
<details>
<summary>展开查看</summary>
<pre><code>.
├─yousj-all
├─yousj-bom
├─yousj-core 核心工具
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
│          │              ├─properties
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
│          │              ├─config
│          │              ├─handler
│          │              └─utils
│          └─resources
│              └─META-INF
├─yousj-datasource 数据库
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
│          │              └─logback
│          └─resources
│              └─META-INF
├─yousj-redis 缓存
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─redis
│          │              ├─cache
│          │              ├─multi
│          │              └─utils
│          └─resources
│              └─META-INF
├─yousj-reload 热部署
│  ├─yousj-reload-class 热部署class[参考ko-time](https://gitee.com/huoyo/ko-time)
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
│  └─yousj-reload-mapper 热部署mapper
│      └─src
│          └─main
│              └─java
│                  └─top
│                      └─yousj
│                          └─reload
│                              ├─controller
│                              └─service
├─yousj-security 权限控制
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─security
│          │              ├─annotation
│          │              ├─config
│          │              ├─exception
│          │              ├─filter
│          │              ├─properties
│          │              └─utils
│          └─resources
│              └─META-INF
├─yousj-swagger 文档
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─swagger
│          └─resources
│              └─META-INF
└─yousj-web
    └─src
        └─main
            ├─java
            │  └─top
            │      └─yousj
            │          └─web
            │              └─config
            └─resources
                └─META-INF
</code></pre>
</details>