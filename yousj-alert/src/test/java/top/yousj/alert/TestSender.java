package top.yousj.alert;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import top.yousj.alert.config.AlertConfig;
import top.yousj.alert.config.DingTalkConfig;
import top.yousj.alert.config.MailConfig;
import top.yousj.alert.config.PushPlusConfig;
import top.yousj.alert.enums.AlertType;
import top.yousj.alert.model.DingTalkMessage;
import top.yousj.alert.model.MailMessage;
import top.yousj.alert.model.Message;
import top.yousj.alert.model.PushPlusMessage;
import top.yousj.alert.sender.AbstractSender;

import java.util.Collections;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestSender {

    @Autowired
    private Map<String, AbstractSender> senderMap;

    private Message message;

    private AlertConfig alertConfig;

    @Test
    public void mailSender() {

        MailMessage mailMessage = new MailMessage();
        mailMessage.setAlertType(AlertType.EMAIL);
        mailMessage.setTo("18037300759@163.com");

        MailConfig mailConfig = new MailConfig();
        mailConfig.setHost("smtp.163.com");
        mailConfig.setFrom("youshaojunde@163.com");
        mailConfig.setAuth(true);
        mailConfig.setUser("youshaojunde@163.com");
        mailConfig.setPass("GAQTMOGBLCAGHVNG");

        message = mailMessage;
        alertConfig = mailConfig;
    }

    @Test
    public void dingTalkSender() {

        DingTalkMessage dingTalkMessage = new DingTalkMessage();
        dingTalkMessage.setAlertType(AlertType.DING_TALK);
        dingTalkMessage.setAtMobiles(Collections.singletonList("18037300759"));

        DingTalkConfig dingTalkConfig = new DingTalkConfig();
        dingTalkConfig.setAccessToken("bb8f38842c3ee83da64d8bf8edf559d8ef4f0802f8bf2ea487a0af8143d9cc8b");
        //dingTalkConfig.setSecret("SEC615ba2e62d8ddb8dd16e7d7532f050829652ddb1e70b958b2de0fc85bf2efd63");

        message = dingTalkMessage;
        alertConfig = dingTalkConfig;
    }

    @Test
    public void pushPlusSender() {

        PushPlusMessage pushPlusMessage = new PushPlusMessage();
        pushPlusMessage.setAlertType(AlertType.PUSH_PLUS);

        PushPlusConfig pushPlusConfig = new PushPlusConfig();
        pushPlusConfig.setToken("4d857c2990114ebe8b89dbceefd1de89");

        message = pushPlusMessage;
        alertConfig = pushPlusConfig;
    }

    @AfterEach
    @SuppressWarnings("unchecked")
    public void after() {
        message.setAtOnce(true);
        message.setTitle("测试一下");
        message.setMsg("hello world");
        senderMap.get(message.getAlertType().getName()).send(message, alertConfig);
    }

}
