package top.yousj.notify.framework;

import org.springframework.stereotype.Service;
import top.yousj.notify.entity.EmailMessage;
import top.yousj.notify.support.AbstractNotify;

@Service
public class EmailNotify extends AbstractNotify<EmailMessage> {

    @Override
    public Object exec(EmailMessage message) {
        return null;
    }

}
