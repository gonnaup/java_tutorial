package org.gonnaup.tutorial.basicknowledge.spi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author gonnaup
 * @version created at 2023/5/9 下午5:08
 */
public class DetailLogger implements ILogger{

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public void debug(String msg) {
        System.out.println(formatter.format(LocalDateTime.now()) + ' ' + Thread.currentThread().getName() + " DEBUG => " + msg);
    }

    @Override
    public void info(String msg) {
        System.out.println(formatter.format(LocalDateTime.now()) + ' ' + Thread.currentThread().getName() + " INFO => " + msg);
    }
}
