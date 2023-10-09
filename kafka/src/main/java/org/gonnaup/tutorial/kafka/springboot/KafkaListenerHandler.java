package org.gonnaup.tutorial.kafka.springboot;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.gonnaup.tutorial.common.domain.JwtString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author gonnaup
 * @version created at 2022/11/17 下午4:08
 */

@Component
public class KafkaListenerHandler {

    private static final Logger log = LoggerFactory.getLogger(KafkaListenerHandler.class);

    @KafkaListener(topics = KafkaConst.TOPIC_JWT)
    public void jwtStringHandler(ConsumerRecord<String, JwtString> data, Acknowledgment acknowledgment) {
//        JWTString jwtString = JsonUtil.parseToObject(data.value(), JWTString.class);
        log.info("receive jwt => {}", data.value());
        acknowledgment.acknowledge();
    }
}
