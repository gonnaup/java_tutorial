package cn.gonnaup.tutorial.rabbitmq.springboot;

import cn.gonnaup.tutorial.common.domain.Commodity;
import cn.gonnaup.tutorial.common.domain.JwtString;
import cn.gonnaup.tutorial.common.domain.Order;
import cn.gonnaup.tutorial.common.util.JsonUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author gonnaup
 * @version created at 2022/11/21 下午12:24
 */
@Slf4j
@Component
public class RabbitMessageHandler {

    private static final Map<String, Class<?>> DICT =
            Map.of(Commodity.class.getSimpleName(), Commodity.class,
                    JwtString.class.getSimpleName(), JwtString.class,
                    Order.class.getSimpleName(), Order.class);

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    void rabbitmqMessageHandler(Channel channel, Message message) {
        final String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        final String type = routingKey.substring(routingKey.lastIndexOf('.') + 1);
        Object msg = JsonUtil.parseToObject(message.getBody(), Objects.requireNonNull(DICT.get(type), "error message type " + type));
        log.info("receive routing key is {}, message is {}", routingKey, msg);
        try {
            //确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            // undo
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
