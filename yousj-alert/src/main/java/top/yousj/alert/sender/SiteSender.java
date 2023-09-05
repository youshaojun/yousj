package top.yousj.alert.sender;

import org.springframework.stereotype.Component;
import top.yousj.alert.config.AlertConfig;
import top.yousj.alert.model.Message;

@Component
public class SiteSender extends AbstractSender {

    @Override
    protected void doSend(Message message, AlertConfig config) {

    }

}
