package top.yousj.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yousj.commons.entity.R;
import top.yousj.commons.utils.SpringUtil;

import java.util.Set;

/**
 * @author yousj
 * @since 2023-01-17
 */
@RestController
@RequestMapping("/uaa")
public class MappingController {

	@GetMapping("/getMappingUrls")
	public R<Set<String>> getMappingUrls() {
		return R.ok(SpringUtil.getMappingUrls());
	}

}
