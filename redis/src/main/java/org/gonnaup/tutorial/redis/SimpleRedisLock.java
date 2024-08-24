package org.gonnaup.tutorial.redis;

import org.redisson.api.RBucket;
import org.redisson.api.RScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.gonnaup.tutorial.redis.RawRedissonClient.redissonClient;

/**
 * @author gonnaup
 * @version created at 2024/7/7 下午5:36
 */
public class SimpleRedisLock {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRedisLock.class);


    public static boolean tryLock(String lockKey, String lockValue, Duration expireTime) {
        RBucket<String> bucket = redissonClient.getBucket(lockKey);
        return bucket.setIfAbsent(lockValue, expireTime);
    }

    public static void unLock(String lockKey, String lockValue) {
        RScript lua = redissonClient.getScript();
        String unlock_script = """
                if redis.call('get', KEYS[1]) == ARGV[1]
                    then
                        return redis.call('del', KEYS[1])
                    else
                        return 0
                end
                """.stripIndent();
        lua.eval(RScript.Mode.READ_WRITE, unlock_script, RScript.ReturnType.INTEGER, List.of(lockKey), lockValue);
    }

    public static void main(String[] args) throws UnknownHostException {
        var key = "00001";
        var value = Inet4Address.getLocalHost().getHostAddress();
        if (tryLock(key, value, Duration.ofMinutes(1))) {
            logger.info("获取锁成功");
            try {
                TimeUnit.SECONDS.sleep(3);
                logger.info("任务完成...");
            } catch (InterruptedException e) {
                //
            } finally {
                unLock(key, value);
                logger.info("解锁完毕...");
            }
        }
        redissonClient.shutdown();
    }

}
