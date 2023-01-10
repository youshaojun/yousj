package top.yousj.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.yousj.log.constant.PropertyConstant;

/**
 * @author yousj
 * @since 2023-01-10
 */
@Data
@Component
@ConfigurationProperties(prefix = PropertyConstant.LOG)
public class LogProperties {

	/**
	 * web日志切面
	 */
	private Aop aop = new Aop();

	@Data
	public static class Aop {

		/**
		 * 排序
		 */
		private Integer order = -1;

		/**
		 * 切面el表达式
		 */
		private String pointcut;

	}

}
