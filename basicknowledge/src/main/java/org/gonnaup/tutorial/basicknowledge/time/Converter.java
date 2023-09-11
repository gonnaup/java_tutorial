package org.gonnaup.tutorial.basicknowledge.time;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 时间api之间的转换
 *
 * @author gonnaup
 * @version created at 2023/9/4 19:44
 */
@Slf4j
public class Converter {

    private static final DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter dateTimeFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void converteToMilliSeconds() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        log.info("当前日期 {} 转时间戳 {}", localDate.format(dateFormater), localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

        log.info("当前时间 {} 转时间戳 {}", localDateTime.format(dateTimeFormater), localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    public static void fromMillSeconds() {
        long timeMillis = System.currentTimeMillis();
        LocalDate localDate = LocalDate.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault());
        log.info("时间戳 {} 转日期 => {}", timeMillis, localDate.format(dateFormater));
        LocalDateTime localDateTime = Instant.ofEpochMilli(timeMillis).atZone(ZoneId.systemDefault()).toLocalDateTime();
        log.info("时间戳 {} 转时间 => {}", timeMillis, localDateTime.format(dateTimeFormater));
    }

    public static void main(String[] args) {
        converteToMilliSeconds();
        fromMillSeconds();
    }
}
