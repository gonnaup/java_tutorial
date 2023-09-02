package org.gonnaup.tutorial.basicknowledge.spi;

/**
 * SPI提供者，需要注册到文件中
 * @author gonnaup
 * @version created at 2023/5/9 下午4:53
 */
public class DefualtLogger implements ILogger{
    @Override
    public void debug(String msg) {
        System.out.println("DEBUG => " + msg);
    }

    @Override
    public void info(String msg) {
        System.out.println("INFO => " + msg);
    }
}
