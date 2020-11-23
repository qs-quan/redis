package com.redis.lua;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-11-16 14:07
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LuaTest {

    @Autowired
    StringRedisTemplate redisTemplate;


    @Test
    public void hello() {

        // 执行 lua 脚本
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/HelloWorld.lua")));
        // 指定返回类型
        redisScript.setResultType(String.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        String result = redisTemplate.execute(redisScript,new ArrayList(),"1");
        System.out.println(result);


    }

}
