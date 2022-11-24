package cn.gonnaup.tutorial.rabbitmq.springboot;

import cn.gonnaup.tutorial.common.domain.Commodity;
import cn.gonnaup.tutorial.common.domain.Order;
import cn.gonnaup.tutorial.common.util.DomainUtil;
import cn.gonnaup.tutorial.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.gonnaup.tutorial.rabbitmq.springboot.RabbitConfig.*;

/**
 * @author gonnaup
 * @version created at 2022/11/23 下午3:44
 */
@Slf4j
@RestController
public class RandomMessageEmitController {

    @Resource
    RabbitTemplate rabbitTemplate;

    /**
     * 发送随机json数据，通过routingKey来判断类型
     *
     * @return
     */
    @GetMapping("/message/random/emit")
    ResponseEntity<Object> emitRandomMessage() {
        Object emit = DomainUtil.randomDomainObject();
        rabbitTemplate.send(EXCHANGE_NAME, SEND_MESSAGE_TOPIC_PREFIX + emit.getClass().getSimpleName(),
                new Message(JsonUtil.toJsonBytes(emit)));
        log.info("Emit the random message type = {}, body = {}", emit.getClass().getSimpleName(), emit);
        return ResponseEntity.ok(emit);

    }


    /**
     * 发送Commodity对象并通过json转换器转换成byte[]
     *
     * @return
     */
    @GetMapping("/message/object/emit/commodity")
    ResponseEntity<Commodity> emitCommodityMessage() {
        Commodity commodity = Commodity.newRandomCommodity();
        rabbitTemplate.convertAndSend(EXCHANGE_OBJECT_NAME, SEND_MESSAGE_OBJECT_TOPIC_PREFIX + Commodity.class.getSimpleName().toLowerCase(), commodity);
        log.info("emit the commodity message, body = {}", commodity);
        return ResponseEntity.ok(commodity);
    }

    /**
     * 发送Order对象
     *
     * @return
     */
    @GetMapping("/message/object/emit/order")
    ResponseEntity<Order> emitOrderMessage() {
        Order order = Order.newRandomOrder();
        rabbitTemplate.convertAndSend(EXCHANGE_OBJECT_NAME, SEND_MESSAGE_OBJECT_TOPIC_PREFIX + Order.class.getSimpleName().toLowerCase(), order);
        log.info("emit the order message, body = {}", order);
        return ResponseEntity.ok(order);
    }


}
