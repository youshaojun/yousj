package top.yousj.commons.properties;

import lombok.Data;

import java.util.List;

/**
 * @author yousj
 * @since 2023-01-10
 */
@Data
public class TimeCostProperties {

	/**
	 * map容量, 尽可能设置的大一些, 避免扩容影响统计
	 */
	private Integer initialCapacity = 1024;

	/**
	 * 超过阈值输出耗时
	 */
	private Integer threshold = 200;

	/**
	 * 排除的bean
	 */
	private List<String> excludes;

}
