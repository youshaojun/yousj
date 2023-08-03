package top.yousj.notify.framework;

import org.springframework.stereotype.Service;
import top.yousj.notify.entity.PushPlusMessage;
import top.yousj.notify.support.AbstractNotify;

import java.util.Map;

@Service
public class PushPlusNotify extends AbstractNotify<PushPlusMessage> {

    @Override
    public Object exec(PushPlusMessage message) {
        return post(message, message.getUrl());
    }

    @Override
    public Map<String, Object> assemble(PushPlusMessage message){
        return message.getParams();
    }

}
