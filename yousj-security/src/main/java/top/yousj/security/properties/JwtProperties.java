package top.yousj.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yousj
 * @since 2023-01-02
 */
@Data
@ConfigurationProperties(prefix = "top.yousj.security.jwt")
public class JwtProperties {
    private Long expire = 86400000L;
    private String signKey = "jwt_secret:";
}
