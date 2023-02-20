package top.yousj.commons.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yousj.commons.enums.ResultCode;

/**
 * 统一返回
 *
 * @author yousj
 * @since 2023-01-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class R<T> {

	private int code;
	private String msg;
	private T data;

	public static <T> R<T> ok(T data) {
		return of(ResultCode.OK, data);
	}

	public static <T> R<T> ok() {
		return of(ResultCode.OK, null);
	}

	public static <T> R<T> fail(T data) {
		return of(ResultCode.SYSTEM_ERROR, data);
	}

	public static <T> R<T> fail(ResultCode resultCode) {
		return of(resultCode.getCode(), resultCode.getValue(), null);
	}

	public static <T> R<T> fail(String msg) {
		return of(ResultCode.SYSTEM_ERROR.getCode(), msg, null);
	}

	public static <T> R<T> fail(int code, T data) {
		return of(code, null, data);
	}

	public static <T> R<T> fail(ResultCode resultCode, T data) {
		return of(resultCode.getCode(), resultCode.getValue(), data);
	}

	public static <T> R<T> fail(int code, String msg, T data) {
		return of(code, msg, data);
	}

	public static <T> R<T> fail(int code) {
		return of(code, null, null);
	}

	public static <T> R<T> fail(int code, String msg) {
		return of(code, msg, null);
	}

	public static <T> R<T> of(ResultCode resultCode, T data) {
		return of(resultCode.getCode(), resultCode.getValue(), data);
	}

	private static <T> R<T> of(int code, String msg, T data) {
		R<T> result = new R<>();
		result.setCode(code);
		result.setMsg(msg);
		result.setData(data);
		return result;
	}

}
