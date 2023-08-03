package top.yousj.notify.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import top.yousj.notify.enums.NotifyTypeEnum;

import java.util.HashMap;
import java.util.Map;

@Data
public class PushPlusMessage extends Message {

    private String url = "https://www.pushplus.plus/send/";

    private Map<String, Object> params;

    /**
     * 构建PushPlusMessage
     *
     * @param token   token
     * @param title   标题
     * @param topic   主题
     * @param content 消息体
     * @return Message
     */
    public Message of(String token, String title, String topic, String content) {
        PushPlusMessage pushPlusMessage = new PushPlusMessage();
        pushPlusMessage.setNotifyType(NotifyTypeEnum.PUSH_PLUS);
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("title", title);
        params.put("content", content);
        if (StringUtils.isNotBlank(token)) {
            params.put("topic", topic);
        }
        pushPlusMessage.setParams(params);
        return pushPlusMessage;
    }

}
