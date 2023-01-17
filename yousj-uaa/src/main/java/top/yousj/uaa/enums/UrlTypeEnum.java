package top.yousj.uaa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UrlTypeEnum {

	IGNORE(0),

	AUTH(1)

	;

	private Integer code;

}
