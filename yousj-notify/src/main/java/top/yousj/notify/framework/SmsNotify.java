package top.yousj.notify.framework;

import org.springframework.stereotype.Service;
import top.yousj.notify.entity.SmsMessage;
import top.yousj.notify.support.AbstractNotify;

@Service
public class SmsNotify extends AbstractNotify<SmsMessage> {

    @Override
    public Object exec(SmsMessage message) {
        return null;
    }

}
