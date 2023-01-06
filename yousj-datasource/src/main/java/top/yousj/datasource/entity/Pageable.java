package top.yousj.datasource.entity;

import lombok.Data;

/**
 * @author yousj
 * @since 2023-01-06
 */
@Data
public class Pageable {

	/**
	 * 页数, 1开始, 默认1
	 */
	private Integer page = 1;

	/**
	 * 每页条数, 默认20
	 */
	private Integer num = 20;

}
