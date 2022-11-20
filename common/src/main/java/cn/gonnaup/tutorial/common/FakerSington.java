package cn.gonnaup.tutorial.common;

import com.github.javafaker.Faker;

import java.util.Locale;

/**
 * Faker 单例
 *
 * @author gonnaup
 * @version created at 2022/11/20 下午8:18
 * @see com.github.javafaker.Faker
 */
public enum FakerSington {

    CN(Locale.CHINA);
    public final Faker FAKER;

    private FakerSington(Locale locale) {
        FAKER = Faker.instance(locale);
    }

}
