# 用于快速构建Java项目而整合的一些常用模块

### 项目结构
<details>
<summary>展开查看</summary>
<pre><code>.
├─yousj-all 所有
├─yousj-bom bom
├─yousj-core 通用工具
│  └─src
│      └─main
│          └─java
│              └─top
│                  └─yousj
│                      └─core
│                          ├─constant
│                          ├─entity
│                          ├─exception
│                          └─utils
├─yousj-datasource 数据库相关
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─datasource
│          │              ├─config
│          │              ├─convert
│          │              └─interceptor
│          └─resources
├─yousj-encrypt 加解密相关
│  └─src
│      └─main
│          └─resources
├─yousj-excel excel相关
│  └─src
│      └─main
│          └─java
│              └─top
│                  └─yousj
│                      └─excel
│                          ├─handler
│                          ├─strategy
│                          └─utils
├─yousj-exception 异常处理相关
│  └─src
│      └─main
│          └─java
│              └─top
│                  └─yousj
│                      └─exception
├─yousj-log 日志处理相关
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
├─yousj-redis redis相关
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─redis
│          │              ├─cache
│          │              ├─multi
│          │              ├─redisson
│          │              └─utils
│          └─resources
│              └─META-INF
├─yousj-reload 热加载相关[class热部署参考](https://gitee.com/huoyo/ko-time)
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─reload
│          │              ├─controller
│          │              └─service
│          └─resources
│              ├─META-INF
│              └─retrans
├─yousj-security auth相关
│  └─src
│      └─main
│          └─java
│              └─top
│                  └─yousj
│                      └─security
│                          ├─config
│                          ├─exception
│                          ├─filter
│                          ├─properties
│                          └─utils
├─yousj-swagger doc相关
│  └─src
│      └─main
│          ├─java
│          │  └─top
│          │      └─yousj
│          │          └─swagger
│          └─resources
└─yousj-web web相关
    └─src
        └─main
            └─java
                └─top
                    └─yousj
                        └─web
                            └─config
</code></pre>
</details>