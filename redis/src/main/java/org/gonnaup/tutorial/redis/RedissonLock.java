package org.gonnaup.tutorial.redis;

import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.gonnaup.tutorial.redis.RawRedissonClient.redissonClient;

/**
 * @author gonnaup
 * @version created at 2024/7/7 下午6:50
 */
public class RedissonLock {

    private static final Logger logger = LoggerFactory.getLogger(RedissonLock.class);


    public static void main(String[] args) {
        var key = "00002";
        RLock lock = RawRedissonClient.redissonClient.getLock(key);
        //
        if (lock.tryLock()) {
            logger.info("获取锁成功");
            try {
                TimeUnit.SECONDS.sleep(35);
                logger.info("任务完成...");
            } catch (InterruptedException e) {
                //
            } finally {
                lock.unlock();
                logger.info("解锁完毕...");
            }
        }
        redissonClient.shutdown();
    }

}
