package top.yousj.alert.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
    private Map<String, AbstractSender> senderMap;

    @Override
    @SuppressWarnings("unchecked")
    public void run(String... args) {
        // TODO 单线程排队发送消息
        executorService.execute(() -> {
            while (true) {
                // TODO select * from message where status = 0;
                // status = 0 => 待发送
                List<Message> messages = null;
                messages.forEach(message -> {
                    // success
                    MsgStatus status = MsgStatus.FAILED;
                    String errorMsg = null;
                    AbstractSender sender = senderMap.get(message.getAlertType().getName());
                    try {
                        sender.send(message);
                        sender.update(message, MsgStatus.SUCCESS, null);
                    } catch (Exception e) {
                        log.error(String.valueOf(message));
                        log.error("send msg failed.", e);
                        errorMsg = e.getMessage();
                    }
                    sender.update(message, status, errorMsg);
                });
            }
        });
    }

}
