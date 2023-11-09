package top.yousj.commons.utils;

import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;
import top.yousj.commons.constant.StrPool;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebUtil extends WebUtils {

    public static boolean isBody(HandlerMethod handlerMethod) {
        ResponseBody responseBody = ClassUtil.getAnnotation(handlerMethod, ResponseBody.class);
        return responseBody != null;
    }

    public static String getCookieVal(String name) {
        Cookie cookie = getCookie(Objects.requireNonNull(getRequest()), name);
        return cookie != null ? cookie.getValue() : null;
    }

    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, null, 0);
    }

    public static void setCookie(HttpServletResponse response, String name, @Nullable String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(StrPool.SLASH);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static void renderJson(HttpServletResponse response, Object result) {
        renderJson(response, result, MediaType.APPLICATION_JSON_VALUE, HttpStatus.OK.value());
    }

    public static void renderJson(HttpServletResponse response, Object result, int status) {
        renderJson(response, result, MediaType.APPLICATION_JSON_VALUE, status);
    }

    @SneakyThrows
    public static void renderJson(HttpServletResponse response, Object result, String contentType, int status) {
        response.setCharacterEncoding(StrPool.CHARSET_NAME);
        response.setContentType(contentType);
        response.setStatus(status);
        try (PrintWriter out = response.getWriter()) {
            out.append(JsonUtil.toJson(result));
            out.flush();
        }
    }

    public static String getIp() {
        return getIp(Objects.requireNonNull(getRequest()));
    }

    public static String getIp(HttpServletRequest request) {
        String ip = null;
        for (String ipHeaderName : StrPool.IP_HEADER_NAMES) {
            if (isUnknown(ip)) {
                ip = request.getHeader(ipHeaderName);
            }
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = StrPool.LOCALHOST;
        }
        if (ip != null && ip.split(StrPool.COMMA).length > 1) {
            ip = ip.split(StrPool.COMMA)[0];
        }
        return ip;
    }

    public static boolean isUnknown(String ip) {
        return ip == null || ip.length() == 0 || StrPool.UNKNOWN.equalsIgnoreCase(ip);
    }

    public static String getHeader(String name) {
        return Objects.requireNonNull(getRequest()).getHeader(name);
    }

    public static Enumeration<String> getHeaders(String name) {
        return Objects.requireNonNull(getRequest()).getHeaders(name);
    }

    public static Enumeration<String> getHeaderNames() {
        return Objects.requireNonNull(getRequest()).getHeaderNames();
    }

    public static String getParameter(String name) {
        return Objects.requireNonNull(getRequest()).getParameter(name);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getParameterMap(MethodInvocation pjp) {
        Map<String, Object> allParameterMap = new HashMap<>();
        if (pjp == null) {
            return allParameterMap;
        }
        Arrays.stream(pjp.getArguments()).filter(e -> !skipType(e)).forEach(e -> allParameterMap.putAll(JsonUtil.fromJson(JsonUtil.toJson(e), Map.class)));
        Map<String, String[]> parameterMap = Objects.requireNonNull(getRequest()).getParameterMap();
        if (CollectionUtils.isEmpty(parameterMap)) {
            return allParameterMap;
        }
        for (String paramName : parameterMap.keySet()) {
            String[] paramValues = parameterMap.get(paramName);
            if (paramValues != null && paramValues.length > 0) {
                String paramValue = paramValues[0];
                allParameterMap.put(paramName, paramValue);
            }
        }
        return allParameterMap;
    }

    public static boolean skipType(Object arg) {
        return arg == null
            || MultipartFile.class.isAssignableFrom(arg.getClass())
            || MultipartFile[].class.isAssignableFrom(arg.getClass())
            || ServletRequest.class.isAssignableFrom(arg.getClass())
            || ServletResponse.class.isAssignableFrom(arg.getClass());
    }

}
