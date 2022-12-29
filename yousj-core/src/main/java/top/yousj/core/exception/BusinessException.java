package top.yousj.core.exception;

import lombok.Data;
import top.yousj.core.entity.ResultCode;

/**
 * @author yousj
 * @since 2022-12-29
 */
@Data
public class BusinessException extends RuntimeException {

	private int code;

	public BusinessException() {
		super(ResultCode.SYSTEM_ERROR.getValue());
		this.code = ResultCode.SYSTEM_ERROR.getCode();
	}

	public BusinessException(String message) {
		super(message);
		this.code = ResultCode.SYSTEM_ERROR.getCode();
	}

	public BusinessException(int code, String message) {
		super(message);
		this.code = code;
	}

	public BusinessException(ResultCode resultCode) {
		super(resultCode.getValue());
		this.code = resultCode.getCode();
	}

	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	public String toString() {
		return this.code + ":" + this.getMessage();
	}

}
