package top.yousj.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

	PARAM_NOT_MATCH(400, "参数不匹配！"),

	UNAUTHORIZED(401, "请先登录！"),

	ACCESS_DENIED(403, "暂无权限！"),

	NOT_FOUND(404, "请求不存在！"),

	UNSUPPORTED_METHOD_TYPE(405, "请求方式不支持！"),

	REQUEST_TOO_LARGE(413, "请求体大小超过限制！"),

	UNSUPPORTED_MEDIA_TYPE(415, "请求格式不支持！"),

	SYSTEM_ERROR(500, "服务器开小差了啦！"),

	;
	private final int code;
	private final String value;

}
