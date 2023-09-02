package org.gonnaup.tutorial.common.util;

import org.gonnaup.tutorial.common.domain.Commodity;
import org.gonnaup.tutorial.common.domain.JwtString;
import org.gonnaup.tutorial.common.domain.Order;

import java.util.concurrent.ThreadLocalRandom;

/**
 * domin对象工具类
 *
 * @author gonnaup
 * @version created at 2022/11/24 下午12:04
 */
public abstract class DomainUtil {


    public static Object randomDomainObject() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int type = random.nextInt(0, 3);
        return switch (type) {
            case 0 -> Commodity.newRandomCommodity();
            case 1 -> JwtString.newRandomJWTString();
            case 2 -> Order.newRandomOrder();
            default -> throw new IllegalArgumentException("");
        };
    }

}
