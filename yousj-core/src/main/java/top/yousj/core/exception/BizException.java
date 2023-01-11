package top.yousj.core.exception;

import lombok.Data;
import top.yousj.core.enums.ResultCode;

/**
 * @author yousj
 * @since 2022-12-29
 */
@Data
public class BizException extends RuntimeException {

	private int code;

	public BizException() {
		super(ResultCode.SYSTEM_ERROR.getValue());
		this.code = ResultCode.SYSTEM_ERROR.getCode();
	}

	public BizException(String message) {
		super(message);
		this.code = ResultCode.SYSTEM_ERROR.getCode();
	}

	public BizException(int code, String message) {
		super(message);
		this.code = code;
	}

	public BizException(ResultCode resultCode) {
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
