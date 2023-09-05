package top.yousj.alert.config;

import lombok.Data;

@Data
public class DingTalkConfig extends AlertConfig {

    private String accessToken;

    private String secret;

    private boolean isAtAll;

    private String msgType = "text";

}