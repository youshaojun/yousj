package top.yousj.alert.config;

import cn.hutool.extra.mail.MailAccount;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @see MailAccount
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MailConfig extends AlertConfig {

    /**
     * SMTP服务器域名
     */
    private String host;
    /**
     * SMTP服务端口
     */
    private Integer port;
    /**
     * 是否需要用户名密码验证
     */
    private Boolean auth;
    /**
     * 用户名
     */
    private String user;
    /**
     * 密码
     */
    private String pass;
    /**
     * 发送方，遵循RFC-822标准
     */
    private String from;

    /**
     * 使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
     */
    private Boolean starttlsEnable = false;
    /**
     * 使用 SSL安全连接
     */
    private Boolean sslEnable;

    /**
     * SMTP SSL信任列表(暂时不用)
     */
    private String sslTrusts;

}