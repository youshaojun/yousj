package top.yousj.security.utils;

import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.util.FieldUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yousj
 * @since 2023-01-02
 */
public class SecurityUtil {

	public static <T> T getUser() {
		return (T) getAuthentication().getPrincipal();
	}

	@SneakyThrows
	public static Integer getUserId() {
		return Integer.valueOf(FieldUtils.getFieldValue(getAuthentication().getPrincipal(), "id").toString());
	}

	/**
	 * 权限集合
	 */
	public static List<String> getAuthorities() {
		return getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
	}

	private static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

}
