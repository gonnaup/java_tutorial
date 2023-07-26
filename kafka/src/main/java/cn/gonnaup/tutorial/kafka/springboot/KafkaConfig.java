package cn.gonnaup.tutorial.kafka.springboot;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

/**
 *
 * 给Json解码器的typeMapper添加信任的包
 *
 * @author gonnaup
 * @version created at 2022/11/17 下午9:19
 * @see org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper
 */
@Slf4j
@Configuration
public class KafkaConfig {

    @Resource
    KafkaProperties kafkaProperties;

    //
    @Bean
    public DefaultKafkaConsumerFactory<?, ?> kafkaConsumerFactory(
            ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> customizers) {
        Map<String, Object> configs = this.kafkaProperties.buildConsumerProperties();
        //添加信任包
        configs.put(JsonDeserializer.TRUSTED_PACKAGES, "cn.gonnaup.tutorial.kafka.springboot");
        DefaultKafkaConsumerFactory<Object, Object> factory = new DefaultKafkaConsumerFactory<>(
                configs);
        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
    }

    @PostConstruct
    void a() {
        //必须为全包名或 前段包名+".*"
        kafkaProperties.getConsumer().getProperties().put(JsonDeserializer.TRUSTED_PACKAGES, "cn.gonnaup.*");
    }

    @Bean
    DefaultKafkaConsumerFactoryCustomizer kafkaConsumerFactoryCustomizer() {
        return consumerFactory -> consumerFactory.updateConfigs(Map.of(JsonDeserializer.TRUSTED_PACKAGES, "cn.gonnaup.*"));
    }

}
