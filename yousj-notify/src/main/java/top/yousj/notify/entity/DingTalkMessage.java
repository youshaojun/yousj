package top.yousj.notify.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.yousj.notify.enums.NotifyTypeEnum;

import java.util.*;

@Data
public class DingTalkMessage extends Message {

    private String url = "https://oapi.dingtalk.com/robot/send";

    private String accessToken;

    private String secret;

    private String msgType = "text";

    private Map<String, Object> at;

    private Map<String, Object> content;

    public Message of(@NotNull String accessToken, @NotNull String secret, boolean isAtAll, @NotNull String msg, String... atMobiles) {
        DingTalkMessage dingTalkMessage = new DingTalkMessage();
        dingTalkMessage.setNotifyType(NotifyTypeEnum.DING_TALK);
        dingTalkMessage.setAccessToken(accessToken);
        dingTalkMessage.setSecret(secret);
        Map<String, Object> content = new HashMap<>();
        content.put("content", msg);
        dingTalkMessage.setContent(content);
        Map<String, Object> at = new HashMap<>();
        at.put("isAtAll", isAtAll);
        at.put("atMobiles", Objects.nonNull(atMobiles) ? Arrays.asList(atMobiles) : Collections.emptyList());
        dingTalkMessage.setAt(at);
        return dingTalkMessage;
    }

}
