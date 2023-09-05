package top.yousj.alert.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MsgStatus {

    AT_ONCE(-1, "立即发送"),

    WAITING(0, "待发送"),

    SUCCESS(1, "发送成功"),

    FAILED(2, "发送失败"),

    ;

    private int code;

    private String des;

}
