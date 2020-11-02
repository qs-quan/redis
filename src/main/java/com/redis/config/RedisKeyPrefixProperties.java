package com.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-10-28 8:48
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis.prefix")
public class RedisKeyPrefixProperties {
    private Boolean enable = Boolean.TRUE;
    private String key;
}
