package top.yousj.uaa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yousj.core.entity.R;
import top.yousj.core.enums.ResultCode;
import top.yousj.security.utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/uaa")
@RequiredArgsConstructor
public class UaaController {

	private final HttpServletRequest request;

	@RequestMapping("/logout")
	public R logout() {
		try {
			JwtUtil.removeToken(request);
		} catch (Exception ignored) {
			return R.fail(ResultCode.UNAUTHORIZED);
		}
		return R.ok();
	}

}
