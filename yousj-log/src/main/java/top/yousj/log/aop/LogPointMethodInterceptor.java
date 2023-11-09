package top.yousj.log.aop;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.yousj.commons.constant.UaaConstant;
import top.yousj.commons.entity.R;
import top.yousj.commons.utils.*;
import top.yousj.log.constant.PropertyConstant;
import top.yousj.log.properties.LogProperties;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author yousj
 * @since 2023-01-05
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = PropertyConstant.LOG, name = "aop.pointcut")
public class LogPointMethodInterceptor implements MethodInterceptor {

    private final LogPointHandler logPointHandler;
    private final LogProperties logProperties;

    @Override
    public Object invoke(MethodInvocation pjp) throws Throwable {
        Object res = null;
        RequestLog requestLog = assemblyRequestLog(pjp);
        try {
            return res = pjp.proceed();
        } finally {
            if (Objects.nonNull(requestLog)) {
                process(requestLog, res);
            }
            MDC.clear();
        }
    }

    private void process(RequestLog requestLog, Object res) {
        Try.run(() -> {
            if (res instanceof R) {
                R r = (R) res;
                requestLog.setResCode(r.getCode());
                requestLog.setResMsg(r.getMsg());
            }
            requestLog.setElapsedTime(ChronoUnit.MILLIS.between(requestLog.getStartTime().toInstant(), Instant.now()));
            logPointHandler.handle(requestLog);
        });
    }


    private RequestLog assemblyRequestLog(MethodInvocation pjp) {
        RequestLog requestLog = null;
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                MDC.put(PropertyConstant.TRACE_ID, String.format("[%s]", UUID.randomUUID().toString()));
                requestLog = new RequestLog();
                requestLog.setStartTime(new Date());
                HttpServletRequest request = attributes.getRequest();
                requestLog.setServerName(request.getServerName());
                requestLog.setUri(request.getRequestURI());
                requestLog.setClassName(pjp.getMethod().getDeclaringClass().getName());
                requestLog.setMethodName(pjp.getMethod().getName());
                requestLog.setRequestMethod(request.getMethod());
                requestLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
                requestLog.setIp(WebUtil.getIp());
                String json = JsonUtil.toJson(WebUtil.getParameterMap(pjp));
                int length = json.length();
                Integer limit = logProperties.getAop().getRequestParamsLimit();
                if (length < limit || limit == 0) {
                    requestLog.setRequestParams(json);
                }
                Integer userId = UaaUtil.getUserId(request);
                requestLog.setUid(userId);
                MDC.put(UaaConstant.FORWARD_AUTH_HEADER_USER_ID, String.format("[%s]", userId));
            }
        } catch (Exception ignored) {
        }
        return requestLog;
    }

}

