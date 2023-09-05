package top.yousj.alert.sender;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;
import top.yousj.alert.config.PushPlusConfig;
import top.yousj.alert.model.PushPlusMessage;

import java.util.HashMap;
import java.util.Map;

@Component
public class PushPlusSender extends AbstractSender<PushPlusMessage, PushPlusConfig> {

    @Override
    protected void doSend(PushPlusMessage message, PushPlusConfig config) {
        post(message, config, "https://www.pushplus.plus/send/");
    }

    @Override
    public Map<String, Object> getParams(PushPlusMessage message, PushPlusConfig config) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", config.getToken());
        params.put("title", message.getTopic());
        params.put("content", message.getMsg());
        if (StrUtil.isNotBlank(message.getTopic())) {
            params.put("topic", message.getTopic());
        }
        return params;
    }

    @Override
    protected Class<PushPlusConfig> getAlertConfigClazz() {
        return PushPlusConfig.class;
    }

}
