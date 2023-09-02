package org.gonnaup.tutorial.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.javafaker.Faker;
import org.gonnaup.tutorial.common.FakerSington;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 模拟订单数据
 *
 * @author gonnaup
 * @version created at 2022/11/20 上午11:13
 */
public record Order(Integer id, Integer comodityId, int quantity, BigDecimal price, char state,
                    //坑：pattern小时使用12小时制 hh 时，必须加上 am,pm匹配符, 即'a'，如"yyyy-MM-dd a hh:mm:ss",
                    //否则time部分解析不成功，导致LocalDateTime解析失败
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createTime) {
    private static final Faker FAKER = FakerSington.CN.FAKER;

    private static final char[] STATE = {'C', 'R', 'F'};

    public static Order newRandomOrder() {
        final Random random = new Random();
        Integer id = random.nextInt(10000, 1000000);
        Integer commodityId = random.nextInt(10000, 1000000);
        int quantity = random.nextInt(1, 100);
        BigDecimal price = new BigDecimal(FAKER.commerce().price());
        char state = STATE[random.nextInt(0, STATE.length)];
        LocalDateTime createTime = LocalDateTime.now();
        return new Order(id, commodityId, quantity, price, state, createTime);
    }
}
