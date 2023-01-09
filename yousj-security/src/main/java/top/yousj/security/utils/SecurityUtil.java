package top.yousj.security.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yousj
 * @since 2023-01-02
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

	/**
	 * 当前用户
	 */
	public static <T> T getCurrentUser() {
		return (T) getAuthentication().getPrincipal();
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
