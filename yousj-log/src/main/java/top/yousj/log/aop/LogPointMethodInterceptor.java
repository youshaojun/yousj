package top.yousj.log.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import top.yousj.core.constant.PropertyConstant;
import top.yousj.core.constant.UaaConstant;
import top.yousj.core.entity.R;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yousj
 * @since 2023-01-05
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = PropertyConstant.LOG, name = "aop.pointcut")
public class LogPointMethodInterceptor implements MethodInterceptor {

	private final ObjectMapper objectMapper;
	private final LogPointHandler logPointHandler;

	@Override
	public Object invoke(MethodInvocation pjp) throws Throwable {
		ServletRequestAttributes attributes = null;
		RequestLog requestLog = null;
		try {
			attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attributes != null) {
				requestLog = new RequestLog();
				requestLog.setStartTime(new Date());
				HttpServletRequest request = attributes.getRequest();
				requestLog.setServerName(request.getServerName());
				requestLog.setUri(request.getRequestURI());
				requestLog.setClassName(pjp.getMethod().getDeclaringClass().getName());
				requestLog.setMethodName(pjp.getMethod().getName());
				requestLog.setRequestMethod(request.getMethod());
				requestLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
				requestLog.setIp(getIpAddr(request));
				Map<String, Object> requestParameterMap = new HashMap<>();
				for (Object arg : pjp.getArguments()) {
					if (arg == null
						|| MultipartFile.class.isAssignableFrom(arg.getClass())
						|| MultipartFile[].class.isAssignableFrom(arg.getClass())
						|| ServletRequest.class.isAssignableFrom(arg.getClass())
						|| ServletResponse.class.isAssignableFrom(arg.getClass())) {
						continue;
					}
					requestParameterMap.putAll(objectMapper.readValue(objectMapper.writeValueAsString(arg), Map.class));
				}
				requestParameterMap.putAll(request.getParameterMap());
				requestLog.setRequestParams(objectMapper.writeValueAsString(requestParameterMap));
				String appUserUid = request.getHeader(UaaConstant.APP_UID);
				if (StringUtils.isNotBlank(appUserUid)) {
					requestLog.setUid(Integer.valueOf(appUserUid));
				}
			}
		} catch (Exception ignored) {
		}

		Object res = pjp.proceed();

		try {
			if (attributes != null) {
				if (requestLog != null && res instanceof R) {
					R r = (R) res;
					requestLog.setResCode(r.getCode());
					requestLog.setResMsg(r.getMsg());
				}
				requestLog.setElapsedTime(ChronoUnit.MILLIS.between(requestLog.getStartTime().toInstant(), Instant.now()));
				logPointHandler.handle(requestLog);
			}
		} catch (Exception ignored) {
		}
		return res;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if ("0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}
		if (ip.split(",").length > 1) {
			ip = ip.split(",")[0];
		}
		return ip;
	}

}

