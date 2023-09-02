package org.gonnaup.tutorial.rabbitmq.springboot;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.gonnaup.tutorial.common.domain.Commodity;
import org.gonnaup.tutorial.common.domain.JwtString;
import org.gonnaup.tutorial.common.domain.Order;
import org.gonnaup.tutorial.common.util.JsonUtil;
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

    /**
     * 手动转换
     *
     * @param channel
     * @param message
     */
    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    void rabbitmqByteMessageHandler(Channel channel, Message message) {
        final String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        final String type = routingKey.substring(routingKey.lastIndexOf('.') + 1);
        Object msg = JsonUtil.parseToObject(message.getBody(), Objects.requireNonNull(DICT.get(type), "error message type " + type));
        log.info("receive routing key is {}, message is {}", routingKey, msg);
        try {
            //确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            // do nothing
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Json MessageConverter
     **/

    @RabbitListener(queues = RabbitConfig.QUEUE_OBJECT_COMMODITY_NAME)
    void rabbitmqCommodityMessageHandler(Commodity commodity, Channel channel, Message message) {
        log.info("receive converted commodity message {}", commodity);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            // do nothing
            log.error("ack failed!");
            throw new RuntimeException(e.getMessage());
        }
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_OBJECT_ORDER_NAME)
    void rabbitmqOrderMessageHandler(Order order, Channel channel, Message message) {
        log.info("receive converted order message {}", order);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            // do nothing
            log.error("ack failed!");
            throw new RuntimeException(e.getMessage());
        }
    }


}
