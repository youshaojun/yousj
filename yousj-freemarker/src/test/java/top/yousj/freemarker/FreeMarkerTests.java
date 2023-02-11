package top.yousj.freemarker;

import org.junit.jupiter.api.Test;
import top.yousj.commons.enums.FileTypeEnum;
import top.yousj.freemarker.utils.FreeMarkUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yousj
 * @since 2023-02-10
 */
public class FreeMarkerTests {

	private static Map<String, Object> data = new HashMap<>();

	static {
		data.put("content", "测试一下");
	}

	@Test
	void asString() {
		System.out.println(FreeMarkUtil.asString(data, "index"));
	}

	@Test
	void process() {
		FreeMarkUtil.process(data, "index", FileTypeEnum.HTML);
	}

}
