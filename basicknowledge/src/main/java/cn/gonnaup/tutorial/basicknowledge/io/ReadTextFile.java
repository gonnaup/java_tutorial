package cn.gonnaup.tutorial.basicknowledge.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * @author gonnaup
 * @version created at 2022/10/24 下午9:02
 */
public class ReadTextFile {
    private static final Logger logger = LoggerFactory.getLogger(ReadTextFile.class);

    public static void readTextFile() {
        String fileClassPath = "/data.txt";
        try (InputStream inputStream = ReadTextFile.class.getResourceAsStream(fileClassPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line = null;
            logger.info("Begin read the {} file  ==>", fileClassPath);
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
        } catch (IOException e) {
            logger.error("error IOException {}", e.getLocalizedMessage());
        }
    }

    public static void main(String[] args) {
        readTextFile();
    }

}
