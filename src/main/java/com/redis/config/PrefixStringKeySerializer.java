package com.redis.config;

import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * ${DESCRIPTION}
 * 对字符串序列化新增前缀
 *
 * @author 14684
 * @create 2020-10-28 8:50
 */

public class PrefixStringKeySerializer extends StringRedisSerializer {
    private Charset charset = StandardCharsets.UTF_8;

    private RedisKeyPrefixProperties prefix;

    PrefixStringKeySerializer(RedisKeyPrefixProperties prefix) {
        super();
        this.prefix = prefix;
    }

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        String saveKey = new String(bytes, charset);
        if (prefix.getEnable() != null && prefix.getEnable()) {
            String prefixKey = spliceKey(prefix.getKey());
            int indexOf = saveKey.indexOf(prefixKey);
            if (indexOf > 0) {
                saveKey = saveKey.substring(indexOf);
            }
        }
        return (saveKey.getBytes() == null ? null : saveKey);
    }

    @Override
    public byte[] serialize(@Nullable String key) {
        if (prefix.getEnable() != null && prefix.getEnable()) {
            key = spliceKey(prefix.getKey()) + key;
        }
        return (key == null ? null : key.getBytes(charset));
    }

    private String spliceKey(String prefixKey) {
        if (!StringUtils.isEmpty(prefixKey) && !prefixKey.endsWith(":")) {
            prefixKey = prefixKey + "::";
        }
        return prefixKey;
    }
}