package org.gonnaup.tutorial.redis;

import org.gonnaup.tutorial.common.util.JsonUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

/**
 * @author gonnaup
 * @version created at 2024/7/7 下午5:36
 */
public class RawRedissonClient {

    public static final RedissonClient redissonClient;

    static {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec(JsonUtil.mapper))
                .useSingleServer()
                .setDatabase(0)
                .setAddress("redis://127.0.0.1:6379")
                .setPassword("123456");
        redissonClient = Redisson.create(config);
    }

}
