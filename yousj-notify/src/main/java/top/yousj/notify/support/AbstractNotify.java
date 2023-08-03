package top.yousj.notify.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import top.yousj.commons.utils.JsonUtil;
import top.yousj.notify.entity.Message;

import java.util.Collections;
import java.util.Map;

@Slf4j
public abstract class AbstractNotify<M extends Message> {

    @Autowired
    protected RestTemplate restTemplate;

    public Object send(M message) {
        try {
            Object result = exec(message);
            log.info("notify result [{}]", JsonUtil.toJson(result));
            return result;
        } catch (Exception e) {
            log.error("notify exec failed.", e);
            throw new RuntimeException(e);
        }
    }

    protected abstract Object exec(M message);

    protected Map<String, Object> assemble(M message) {
        return Collections.emptyMap();
    }

    protected String post(M message, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForEntity(url, new HttpEntity<>(assemble(message), headers), String.class).getBody();
    }


}
