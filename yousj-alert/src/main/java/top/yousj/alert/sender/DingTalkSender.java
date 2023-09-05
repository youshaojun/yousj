package top.yousj.alert.sender;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import top.yousj.alert.config.DingTalkConfig;
import top.yousj.alert.model.DingTalkMessage;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class DingTalkSender extends AbstractSender<DingTalkMessage, DingTalkConfig> {

    @Override
    protected void doSend(DingTalkMessage message, DingTalkConfig config) {
        String url = String.format("https://oapi.dingtalk.com/robot/send?access_token=%s", config.getAccessToken());

        String secret = config.getSecret();
        if (StrUtil.isNotBlank(secret)) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String sign = dingHmacSHA256(timestamp, config.getSecret());
            url = String.format("%s&timestamp=%s&sign=%s", url, timestamp, sign);
        }
        post(message, config, url);
    }

    @Override
    protected Class<DingTalkConfig> getAlertConfigClazz() {
        return DingTalkConfig.class;
    }

    @Override
    public Map<String, Object> getParams(DingTalkMessage message, DingTalkConfig config) {
        Map<String, Object> params = new HashMap<>();
        params.put("msgtype", config.getMsgType());

        Map<String, Object> content = new HashMap<>();
        content.put("content", message.getMsg());
        params.put("text", content);

        Map<String, Object> at = new HashMap<>();
        at.put("isAtAll", config.isAtAll());

        at.put("atMobiles", ObjectUtil.defaultIfNull(message.getAtMobiles(), Collections.emptyList()));
        params.put("at", at);
        return params;
    }

    @SneakyThrows
    private String dingHmacSHA256(String timestamp, String secret) {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        Charset charset = StandardCharsets.UTF_8;
        mac.init(new SecretKeySpec(secret.getBytes(charset), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(charset));
        return URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), charset);
    }

}
