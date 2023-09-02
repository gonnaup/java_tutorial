package org.gonnaup.tutorial.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * json序列化和反序列化工具类
 *
 * @author gonnaup
 * @version created at 2022/11/17 下午3:22
 */
public final class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private JsonUtil() {
    }

    public static final ObjectMapper mapper;

    static {
        mapper = JsonMapper.builder()
                .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModules(new Jdk8Module(), new JavaTimeModule())
                .build();
    }

    public static <T> String toJsonString(T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("json转换错误");
        }
    }

    public static <T> T parseToObject(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("错误的json串");
        }
    }


    public static <T> byte[] toJsonBytes(T t) {
        try {
            return mapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("json转换错误");
        }
    }

    public static <T> T parseToObject(byte[] json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("错误的json串", e.getCause());
        }
    }

}
