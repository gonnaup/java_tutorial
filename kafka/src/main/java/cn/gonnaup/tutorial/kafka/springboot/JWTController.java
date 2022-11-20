package cn.gonnaup.tutorial.kafka.springboot;

import cn.gonnaup.tutorial.common.domain.JwtString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author gonnaup
 * @version created at 2022/11/17 下午4:03
 */
@Slf4j
@RestController
public class JWTController {

    @Resource
    KafkaTemplate<String, JwtString> kafkaTemplate;

    @GetMapping("/jwt/launch")
    ResponseEntity<JwtString> lanuchJWTString() throws ExecutionException, InterruptedException {
        JwtString jwtString = JwtString.randomJWTString();
        ListenableFuture<SendResult<String, JwtString>> send = kafkaTemplate.send(KafkaConst.TOPIC_JWT, jwtString);
        SendResult<String, JwtString> result = send.completable().get();

        log.info("send the object JWTString => {}", result.getProducerRecord().value());
        return ResponseEntity.ok(jwtString);
    }

}
