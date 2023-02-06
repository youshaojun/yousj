package top.yousj.uaa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.yousj.commons.entity.R;
import top.yousj.security.utils.JwtUtil;
import top.yousj.uaa.entity.vo.request.LoginRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/uaa")
@RequiredArgsConstructor
public class UaaController {

	private final HttpServletRequest request;

	@PostMapping("/login")
		public R login(@RequestBody @Valid LoginRequest loginRequest) {
		// TODO 统一登录
		return R.ok();
	}

	@GetMapping("/logout")
	public R<Boolean> logout() {
		return R.ok(JwtUtil.removeToken(request));
	}

}
