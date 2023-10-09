package org.gonnaup.tutorial.kafka.springboot;

import jakarta.annotation.Resource;
import org.gonnaup.tutorial.common.domain.JwtString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author gonnaup
 * @version created at 2022/11/17 下午4:03
 */
@RestController
public class JWTController {

    private static final Logger log = LoggerFactory.getLogger(JWTController.class);

    @Resource
    KafkaTemplate<String, JwtString> kafkaTemplate;

    @GetMapping("/jwt/launch")
    ResponseEntity<JwtString> lanuchJWTString() throws ExecutionException, InterruptedException {
        JwtString jwtString = JwtString.newRandomJWTString();
        CompletableFuture<SendResult<String, JwtString>> send = kafkaTemplate.send(KafkaConst.TOPIC_JWT, jwtString);
        SendResult<String, JwtString> result = send.get();

        log.info("send the object JWTString => {}", result.getProducerRecord().value());
        return ResponseEntity.ok(jwtString);
    }

}
