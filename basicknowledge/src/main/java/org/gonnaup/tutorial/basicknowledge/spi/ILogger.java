package org.gonnaup.tutorial.basicknowledge.spi;

/**
 * 定义日志接口
 * SPI接口，实现类需注册到文件[META-INF/services/接口全限定名]中
 *
 * @author gonnaup
 * @version created at 2023/5/9 下午4:33
 */
public interface ILogger {
    void debug(String msg);

    void info(String msg);
}
