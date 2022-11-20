package cn.gonnaup.tutorial.common.domain;

import cn.gonnaup.tutorial.common.FakerSington;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * @author gonnaup
 * @version created at 2022/11/20 上午11:13
 */
public record Order(Integer id, Integer comodityId, int quantity, BigDecimal price, char state,
                    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") LocalDateTime createTime) {
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
