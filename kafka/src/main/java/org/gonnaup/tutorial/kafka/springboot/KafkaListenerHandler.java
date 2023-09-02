package org.gonnaup.tutorial.kafka.springboot;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.gonnaup.tutorial.common.domain.JwtString;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author gonnaup
 * @version created at 2022/11/17 下午4:08
 */

@Slf4j
@Component
public class KafkaListenerHandler {


    @KafkaListener(topics = KafkaConst.TOPIC_JWT)
    public void jwtStringHandler(ConsumerRecord<String, JwtString> data, Acknowledgment acknowledgment) {
//        JWTString jwtString = JsonUtil.parseToObject(data.value(), JWTString.class);
        log.info("receive jwt => {}", data.value());
        acknowledgment.acknowledge();
    }
}
