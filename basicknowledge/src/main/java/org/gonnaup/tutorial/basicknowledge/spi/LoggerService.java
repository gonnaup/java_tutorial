package org.gonnaup.tutorial.basicknowledge.spi;

import java.util.List;
import java.util.ServiceLoader;

/**
 * @author gonnaup
 * @version created at 2023/5/9 下午4:36
 */
public class LoggerService {

    private final ILogger logger;

    private final List<ILogger> loggerList;

    private LoggerService() {
        //SPI实现加载
        ServiceLoader<ILogger> loader = ServiceLoader.load(ILogger.class);
        loggerList = loader.stream().map(ServiceLoader.Provider::get).toList();
        logger = loggerList.isEmpty() ? null : loggerList.get(0);
    }

    public static LoggerService instance() {
        return LoggerServiceHolder.INSTANCE;
    }

    public void debug(String msg) {
        logger.debug(msg);
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void debugAll(String msg) {
        loggerList.forEach(iLogger -> iLogger.debug(msg));
    }


    //静态内部类实现单例模式
    private static class LoggerServiceHolder {
        private static final LoggerService INSTANCE = new LoggerService();
    }

    public static void main(String[] args) {
        LoggerService instance = LoggerService.instance();
        instance.debug("debug信息");
        instance.debugAll("debugAll信息");
        instance.info("info信息");
    }

}
