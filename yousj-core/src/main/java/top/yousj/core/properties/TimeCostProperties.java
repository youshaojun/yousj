package top.yousj.core.properties;

import lombok.Data;

/**
 * @author yousj
 * @since 2023-01-10
 */
@Data
public class TimeCostProperties {

	private int initialCapacity = 1024;

	private int threshold = 200;

	private String[] excludes = new String[]{};

}
