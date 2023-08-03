package top.yousj.notify.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.yousj.notify.framework.*;
import top.yousj.notify.support.AbstractNotify;

@Getter
@AllArgsConstructor
public enum NotifyTypeEnum {

    DING_TALK(DingTalkNotify.class),

    EMAIL(EmailNotify.class),

    PUSH_PLUS(PushPlusNotify.class),

    SMS(SmsNotify.class),

    ;

    private Class<? extends AbstractNotify> clazz;

}
