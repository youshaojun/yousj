package top.yousj.uaa.entity.po;

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
 * 用户数据源
 * </p>
 *
 * @author yousj
 * @since 2023-01-15
 */
@Data
@TableName("uaa_user_data_source")
@ApiModel(value = "UaaUserDataSource对象", description = "用户数据源")
public class UaaUserDataSource implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty("应用标志")
	private String appName;

	@ApiModelProperty("数据源名称")
	private String ds;

	@ApiModelProperty("JDBC 用户名")
	private String username;

	@ApiModelProperty("JDBC 密码")
	private String password;

	@ApiModelProperty("JDBC driver")
	private String driverClassName;

	@ApiModelProperty("JDBC url 地址")
	private String url;

	@ApiModelProperty("查询用户信息的sql")
	private String queryUserAuthSql;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}
