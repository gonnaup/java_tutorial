package cn.gonnaup.tutorial.common.domain;

import cn.gonnaup.tutorial.common.FakerSington;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.Random;

/**
 * 模拟商品数据
 *
 * @author gonnaup
 * @version created at 2022/11/20 上午11:16
 */
public record Commodity(Integer id, String name, String origin, String picture_url, BigDecimal price, String originSite,
                        String discribe) {

    private static final Faker FAKER = FakerSington.CN.FAKER;

    public static Commodity newRandomCommodity() {
        Integer id = new Random().nextInt(10000, 10000000);
        String name = FAKER.commerce().productName();
        String origin = FAKER.address().state();
        String picture = FAKER.avatar().image();
        BigDecimal price = new BigDecimal(FAKER.commerce().price());
        String originSite = FAKER.internet().url();
        String describe = FAKER.yoda().quote();
        return new Commodity(id, name, origin, picture, price, originSite, describe);
    }
}
