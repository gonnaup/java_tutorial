package org.gonnaup.tutorial.common.domain;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

/**
 * 模拟jwt数据
 *
 * @author gonnaup
 * @version created at 2022/11/19 下午8:58
 */
public record JwtString(Integer id, String jwt, Long timestamps) {

    public static JwtString newRandomJWTString() {
        return new JwtString(new Random().nextInt(10000, Integer.MAX_VALUE),
                Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)), System.currentTimeMillis());
    }

}
