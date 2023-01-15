package top.yousj.uaa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 过滤path配置表
 * </p>
 *
 * @author yousj
 * @since 2023-01-15
 */
@Data
@TableName("uaa_auth_url_config")
@ApiModel(value = "UaaAuthUrlConfig对象", description = "过滤path配置表")
public class UaaAuthUrlConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty("应用标志")
	private String appName;

	private String authUrl;

	@ApiModelProperty("url类型, 0 直接放行, 1 登录后可访问, 2 全部url映射")
	private Integer urlType;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@ApiModelProperty("是否删除 0 否, 1 是")
	private Integer isDeleted;

}
