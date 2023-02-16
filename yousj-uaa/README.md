# 统一认证服务 **[实验]**

使用[apisix网关](https://apisix.apache.org/zh/)

[apisix docker example](https://github.com/apache/apisix-docker/tree/master/example)

配置forward-auth插件
```json
{
  "disable": false,
  "request_headers": ["Authorization", "Top-Yousj-App-Name"],
  "timeout": 60000,
  "upstream_headers": ["X-User-ID"],
  "uri": "http://host.docker.internal:8080"
}
```