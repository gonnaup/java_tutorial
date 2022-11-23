package cn.gonnaup.tutorial.rabbitmq.springboot;

import cn.gonnaup.tutorial.common.domain.Commodity;
import cn.gonnaup.tutorial.common.domain.JwtString;
import cn.gonnaup.tutorial.common.domain.Order;
import cn.gonnaup.tutorial.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author gonnaup
 * @version created at 2022/11/23 下午3:44
 */
@Slf4j
@RestController
public class RandomMessageEmitController {

    @Resource
    RabbitTemplate rabbitTemplate;

    @GetMapping("/message/random/emit")
    ResponseEntity<Object> emitRandomMessage() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int type = random.nextInt(0, 3);
        Object emit = switch (type) {
            case 0 -> Commodity.newRandomCommodity();
            case 1 -> JwtString.newRandomJWTString();
            case 2 -> Order.newRandomOrder();
            default -> throw new IllegalArgumentException("");
        };
        rabbitTemplate.send(RabbitConfig.EXCHANGE_NAME, RabbitConfig.SEND_MESSAGE_TOPIC_PREFIX + emit.getClass().getSimpleName(),
                new Message(JsonUtil.toJsonBytes(emit)));
        log.info("Emit the random message type = {}, body = {}", emit.getClass().getSimpleName(), emit);
        return ResponseEntity.ok(emit);

    }


}
