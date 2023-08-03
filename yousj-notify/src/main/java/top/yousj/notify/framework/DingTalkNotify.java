package top.yousj.notify.framework;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import top.yousj.notify.entity.DingTalkMessage;
import top.yousj.notify.support.AbstractNotify;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class DingTalkNotify extends AbstractNotify<DingTalkMessage> {

    @Override
    public Object exec(DingTalkMessage message) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String sign = dingHmacSHA256(timestamp, message.getSecret());
        String url = String.format("https://oapi.dingtalk.com/robot/send?access_token=%s&timestamp=%s&sign=%s", message.getAccessToken(), timestamp, sign);
        return post(message, url);
    }

    @Override
    public Map<String, Object> assemble(DingTalkMessage message) {
        Map<String, Object> params = new HashMap<>();
        params.put("msgtype", message.getMsgType());
        params.put("text", message.getContent());
        params.put("at", message.getAt());
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
