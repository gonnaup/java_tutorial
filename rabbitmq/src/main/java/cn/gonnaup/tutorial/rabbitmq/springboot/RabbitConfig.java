package cn.gonnaup.tutorial.rabbitmq.springboot;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gonnaup
 * @version created at 2022/11/21 上午11:13
 */
@Configuration
public class RabbitConfig {

    static final String EXCHANGE_NAME = "exchange.tutorial.rabbitmq.springboot";

    static final String QUEUE_NAME = "queue.tutorial.rabbitmq.springboot";

    //队列的 routing_key
    static final String QUEUE_ROUTING_KEY = "topic.tutorial.rabbitmq.message.*";

    //发送消息的 routing_key 前缀
    static final String SEND_MESSAGE_TOPIC_PREFIX = "topic.tutorial.rabbitmq.message.";


    @Bean
    Queue declareQueue() {
        return new Queue(QUEUE_NAME, true, false, false);
    }

    @Bean
    Exchange declareExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }


    @Bean
    Binding declareBinding() {
        return BindingBuilder.bind(declareQueue()).to(declareExchange()).with(QUEUE_ROUTING_KEY).noargs();
    }

}
