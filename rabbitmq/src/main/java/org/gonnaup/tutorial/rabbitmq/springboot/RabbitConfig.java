package org.gonnaup.tutorial.rabbitmq.springboot;

import org.gonnaup.tutorial.common.util.JsonUtil;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author gonnaup
 * @version created at 2022/11/21 上午11:13
 */
@Configuration
public class RabbitConfig {

    /**
     * 手动转换 相关
     **/
    static final String EXCHANGE_NAME = "exchange.tutorial.rabbitmq.springboot";

    static final String QUEUE_NAME = "queue.tutorial.rabbitmq.springboot";

    //队列的 routing_key
    static final String QUEUE_ROUTING_KEY = "topic.tutorial.rabbitmq.message.*";

    //发送消息的 routing_key 前缀
    static final String SEND_MESSAGE_TOPIC_PREFIX = "topic.tutorial.rabbitmq.message.";


    /**
     * 使用json messageconverter 相关
     * 使用direct类型 Exchange，将不同类型数据路由到不同队列
     */

    static final String EXCHANGE_OBJECT_NAME = "exchange.tutorial.rabbitmq.springboot.object";

    static final String QUEUE_OBJECT_COMMODITY_NAME = "queue.tutorial.rabbitmq.springboot.object.commodity";

    static final String QUEUE_OBJECT_ORDER_NAME = "queue.tutorial.rabbitmq.springboot.object.order";

    static final String SEND_MESSAGE_OBJECT_TOPIC_PREFIX = "topic.tutorial.rabbitmq.message.object.";

    static final String QUEUE_OBJECT_ROUTING_KEY_COMMODIDY = SEND_MESSAGE_OBJECT_TOPIC_PREFIX + "commodity";

    static final String QUEUE_OBJECT_ROUTING_KEY_ORDER = SEND_MESSAGE_OBJECT_TOPIC_PREFIX + "order";


    @Bean
    Queue declareQueue() {
        return new Queue(QUEUE_NAME, true, false, false);
    }

    @Bean
    Queue declareCommodityQueue() {
        return new Queue(QUEUE_OBJECT_COMMODITY_NAME, true, false, false);
    }

    @Bean
    Queue declareOrderQueue() {
        return new Queue(QUEUE_OBJECT_ORDER_NAME, true, false, false);
    }

    @Bean
    Exchange declareTopicExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    Exchange declareDirectExchange() {
        return new DirectExchange(EXCHANGE_OBJECT_NAME, true, false);
    }


    @Bean
    Binding declareBinding() {
        return BindingBuilder.bind(declareQueue()).to(declareTopicExchange()).with(QUEUE_ROUTING_KEY).noargs();
    }

    @Bean
    Binding declareCommodityQueueBinding() {
        return BindingBuilder.bind(declareCommodityQueue()).to(declareDirectExchange()).with(QUEUE_OBJECT_ROUTING_KEY_COMMODIDY).noargs();
    }

    @Bean
    Binding declareOrderQueueBinding() {
        return BindingBuilder.bind(declareOrderQueue()).to(declareDirectExchange()).with(QUEUE_OBJECT_ROUTING_KEY_ORDER).noargs();
    }


    /**
     * json 转换器
     * 原理，将javaType以默认key "__TypeId__", value为类型的Header中
     *
     * @see org.springframework.amqp.support.converter.AbstractJavaTypeMapper
     * @see org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.RabbitTemplateConfiguration#rabbitTemplateConfigurer(RabbitProperties, ObjectProvider, ObjectProvider)
     */
    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(JsonUtil.mapper, "org.gonnaup.tutorial.*");
    }

    /**
     * 基于RoutingKey为类型鉴别器的Json Converter
     * routingKey 规则： "xxx.xxx...xxx.type"
     */
    static class RoutingKeyBaseJsonMessageConverter implements MessageConverter {

        private final Map<String, Class<?>> classMap;

        public RoutingKeyBaseJsonMessageConverter(Map<String, Class<?>> classMap) {
            this.classMap = classMap;
        }

        @Override
        public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
            return new Message(JsonUtil.toJsonBytes(object), messageProperties);
        }

        @Override
        public Object fromMessage(Message message) throws MessageConversionException {
            final String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            //截取最后的字符串为 javaType
            String type = routingKey.substring(routingKey.lastIndexOf('.') + 1);
            Class<?> javaType = classMap.getOrDefault(type, Map.class);
            return JsonUtil.parseToObject(message.getBody(), javaType);
        }

    }

}
