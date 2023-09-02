package org.gonnaup.tutorial.basicknowledge.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * jdk 动态代理
 *
 * @author gonnaup
 * @version created at 2022/10/23 下午4:29
 */
public class JdkProxy {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        FileTranster target = new FileTranster();
        Transter<File> fileTranster = (Transter<File>) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                new Class[]{Transter.class}, new TransterInvocationHandler(target));
        if (fileTranster.connect() == 0) {
            fileTranster.transaction(null);
            fileTranster.transaction(new File(Objects.requireNonNull(JdkProxy.class.getResource("/logback.xml")).getFile()));
            fileTranster.close();
        }
    }


    static class TransterInvocationHandler implements InvocationHandler {
        private static final Logger logger = LoggerFactory.getLogger(TransterInvocationHandler.class);
        private final Object target;

        public TransterInvocationHandler(Object target) {
            Objects.requireNonNull(target);
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //在动态代理方法中插入默认文件
            if ("transaction".equals(method.getName())) {
                File file = null;
                if (args[0] == null) {
                    logger.warn("arg is null, using the default file [data.txt]");
                    URL resource = target.getClass().getResource("/data.txt"); //文件前必须加"/"才能获取资源
                    Objects.requireNonNull(resource, "Default file can't be null!");
                    String fileName = resource.getFile();
                    file = new File(fileName);
                } else {
                    file = (File) args[0];
                }
                return method.invoke(target, file);
            }
            return method.invoke(target, args);
        }
    }

    interface Transter<T> {
        int connect();

        int transaction(T t);

        int close();
    }

    static class FileTranster implements Transter<File> {
        private static final Logger logger = LoggerFactory.getLogger(FileTranster.class);

        @Override
        public int connect() {
            logger.info("connect OK...");
            return 0;
        }

        @Override
        public int transaction(File file) {
            logger.info("print the file {} content", file.getAbsolutePath());
            try (Stream<String> lines = Files.lines(Paths.get(file.toURI()))) {
                lines.forEach(logger::info);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            logger.info("file transaction OK...");
            return 0;
        }

        @Override
        public int close() {
            logger.info("file transaction closed OK...");
            return 0;
        }
    }
}


