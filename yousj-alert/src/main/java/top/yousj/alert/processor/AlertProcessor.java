package top.yousj.alert.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import top.yousj.alert.config.AlertConfig;
import top.yousj.alert.enums.MsgStatus;
import top.yousj.alert.model.Message;
import top.yousj.alert.sender.AbstractSender;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
//@Component
public class AlertProcessor implements CommandLineRunner {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private Map<String, AbstractSender<Message, AlertConfig>> senderMap;

    @Override
    public void run(String... args) {
        // TODO 单线程排队发送消息
        executorService.execute(() -> {
            while (true) {
                // TODO select * from message where status = 0;
                // status = 0 => 待发送
                List<Message> messages = null;
                messages.forEach(message -> {
                    // success
                    MsgStatus status = MsgStatus.WAITING;
                    try {
                        senderMap.get(message.getAlertType().getName()).send(message);
                        status = MsgStatus.SUCCESS;
                    } catch (Exception e) {
                        log.error(String.valueOf(message));
                        log.error("send msg failed.", e);
                        // failed
                        status = MsgStatus.FAILED;
                    }
                    // TODO update log
                });
            }
        });
    }

}
