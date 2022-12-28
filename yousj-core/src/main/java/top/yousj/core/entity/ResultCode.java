package top.yousj.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    UNAUTHORIZED(401, "请先登录！"),

    ACCESS_DENIED(403, "暂无权限！"),

    SYSTEM_ERROR(500, "服务器开小差了啦！"),

    ;
    private final int code;
    private final String value;

}
