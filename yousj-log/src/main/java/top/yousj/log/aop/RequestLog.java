package top.yousj.log.aop;

import lombok.Data;

import java.util.Date;

/**
 * @author yousj
 * @since 2023-01-04
 */
@Data
public class RequestLog {

	/**
	 * 响应码
	 */
	private Integer resCode;

	/**
	 * 响应信息
	 */
	private String resMsg;

	/**
	 * 用户id
	 */
	private Integer uid;

	/**
	 * 服务名称
	 */
	private String serverName;

	/**
	 * 请求路径
	 */
	private String uri;

	/**
	 * 类名
	 */
	private String className;

	/**
	 * 方法名
	 */
	private String methodName;

	/**
	 * 请求方式
	 */
	private String requestMethod;

	/**
	 * User-Agent
	 */
	private String userAgent;

	/**
	 * ip
	 */
	private String ip;

	/**
	 * 请求参数
	 */
	private String requestParams;

	/**
	 * 请求开始时间
	 */
	private Date startTime;

	/**
	 * 接口耗时
	 */
	private Long elapsedTime;

}
