package top.yousj.notify.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;
import top.yousj.commons.utils.SpringUtil;
import top.yousj.notify.entity.Message;
import top.yousj.notify.enums.NotifyTypeEnum;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotifyHelper {

    @SuppressWarnings("unchecked")
    public static Object send(@NotNull Message message) {
        NotifyTypeEnum notifyType = message.getNotifyType();
        Assert.notNull(notifyType, "notifyType must not be null.");
        AbstractNotify notifyBean = SpringUtil.getBean(notifyType.getClazz());
        Assert.notNull(notifyType, "notifyBean not found.");
        return notifyBean.send(message);
    }

}
