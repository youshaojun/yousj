package top.yousj.alert.sender;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.client.RestOperations;
import top.yousj.commons.utils.JsonUtil;
import top.yousj.alert.config.AlertConfig;
import top.yousj.alert.enums.MsgStatus;
import top.yousj.alert.model.Message;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public abstract class AbstractSender<M extends Message, C extends AlertConfig> {

    @Autowired
    private RestOperations restTemplate;

    private static final PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}");

    /**
     * 执行消息
     *
     * @param message 消息体
     * @return 最终发送的消息内容
     */
    public String send(M message) {
        return send(message, null);
    }

    public String send(M message, C config) {
        Assert.notNull(message, "消息不能为空.");
        replace(message);
        if (config == null) {
            config = parseConfig(message);
        }
        if (message.isAtOnce()) {
            try {
                save(message, MsgStatus.AT_ONCE);
                doSend(message, config);
                update(message, MsgStatus.SUCCESS, null);
            } catch (Exception e) {
                log.error(String.valueOf(message));
                log.error("消息发送失败: ", e);
                update(message, MsgStatus.FAILED, e.getMessage());
            }
        } else {
            save(message, MsgStatus.WAITING);
        }
        return message.getMsg();
    }

    protected abstract void doSend(M message, C config);

    public void save(M message, MsgStatus status) {
        // TODO save log
    }

    public void update(M message, MsgStatus status, String errorMsg) {
        // TODO update log
    }

    /**
     * 通知配置类
     */
    protected Class<C> getAlertConfigClazz() {
        return null;
    }

    protected C parseConfig(M message) {
        Class<C> alertConfigClazz = getAlertConfigClazz();
        if (Objects.isNull(alertConfigClazz)) {
            return null;
        }
        return JsonUtil.fromJson(getAlertConfigStr(message), alertConfigClazz);
    }

    protected String getAlertConfigStr(M message) {
        // TODO select alert_config from app_config where tenant_id = #{tenantId} and alert_type = #{code} and app_name = #{appName}
        return null;
    }

    /**
     * 转换占位符解析
     */
    protected void replace(M message) {
        if (Objects.nonNull(message.getTemplateId()) && CollUtil.isNotEmpty(message.getParams())) {
            Properties props = new Properties();
            for (Map.Entry<String, Object> entry : message.getParams().entrySet()) {
                props.setProperty(entry.getKey(), String.valueOf(entry.getValue()));
            }
            message.setMsg(placeholderHelper.replacePlaceholders(getTemplate(message.getTemplateId()), props));
        }
    }

    protected String getTemplate(Long templateId) {
        // TODO select template from message_template where id = #{templateId}
        return "";
    }

    protected Map<String, Object> getParams(M message, C config) {
        return Collections.emptyMap();
    }

    protected String post(M message, C config, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForEntity(url, new HttpEntity<>(getParams(message, config), headers), String.class).getBody();
    }

}
