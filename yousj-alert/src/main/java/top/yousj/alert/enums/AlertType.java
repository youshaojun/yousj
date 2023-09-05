package top.yousj.alert.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum AlertType {

    SITE_MESSAGE(0, "siteSender", "消息(站内信)"),

    EMAIL(1, "mailSender", "邮件"),

    DING_TALK(2, "dingTalkSender", "钉钉"),

    PUSH_PLUS(3, "pushPlusSender", "PUSH_PLUS"),

    ;

    private Integer code;

    private String name;

    private String des;

    public static List<AlertType> ALL = Arrays.stream(values()).collect(Collectors.toList());

    public static AlertType of(Integer code) {
        return ALL.stream().filter(e -> Objects.equals(code, e.getCode())).findFirst().orElseThrow(() -> new RuntimeException("未知的通知类型!"));
    }

}
