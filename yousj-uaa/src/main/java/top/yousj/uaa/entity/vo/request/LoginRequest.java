package top.yousj.uaa.entity.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author yousj
 * @since 2023-01-16
 */
@Data
public class LoginRequest {

	/**
	 * 账号
	 */
	@NotBlank(message = "账号不能为空")
	@Length(max = 255, message = "账号长度超过限制")
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message = "密码不能为空")
	private String password;

}
