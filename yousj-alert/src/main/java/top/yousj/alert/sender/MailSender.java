package top.yousj.alert.sender;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.stereotype.Component;
import top.yousj.alert.config.MailConfig;
import top.yousj.alert.model.MailMessage;

@Component
public class MailSender extends AbstractSender<MailMessage, MailConfig> {

    @Override
    public void doSend(MailMessage message, MailConfig config) {
        MailAccount mailAccount = new MailAccount();
        PropertyMapper propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        propertyMapper.from(config.getHost()).to(mailAccount::setHost);
        propertyMapper.from(config.getPort()).to(mailAccount::setPort);
        propertyMapper.from(config.getFrom()).to(mailAccount::setFrom);
        propertyMapper.from(config.getStarttlsEnable()).to(mailAccount::setStarttlsEnable);
        propertyMapper.from(config.getSslEnable()).to(mailAccount::setSslEnable);
        propertyMapper.from(config.getAuth()).toCall(() -> {
            propertyMapper.from(config.getUser()).to(mailAccount::setUser);
            propertyMapper.from(config.getPass()).to(mailAccount::setPass);
        });
        MailUtil.send(mailAccount, message.getTo(), message.getTitle(), message.getMsg(), message.isHtml());
    }

    @Override
    protected Class<MailConfig> getAlertConfigClazz() {
        return MailConfig.class;
    }

}
