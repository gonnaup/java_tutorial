package org.gonnaup.tutorial.rabbitmq;

import com.rabbitmq.client.*;
import org.gonnaup.tutorial.common.domain.JwtString;
import org.gonnaup.tutorial.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用Rabbitmq client 访问rabbitmq服务
 * <p>
 * Exchange类型：<code>direct</code>, <code>fanout</code>, <code>topic</code>, <code>headers</code>
 * </p>
 * <a href="https://note.youdao.com/s/GcAROkGm">tutorial</a>
 *
 * @author gonnaup
 * @version created at 2022/11/19 下午2:00
 */
public class RabbitmqRaw {

    private static final Logger log = LoggerFactory.getLogger(RabbitmqRaw.class);

    //'#'匹配0～n个单词，'*'匹配一个单词
    static final String TOPIC_ROUTING_KEY = "topic.token.*";

    static final String TOPIC_EXCHANGE_NAME = "exchange_token";

    /**
     * 自定义线程工厂
     */
    static class CustomerThreadFactory implements ThreadFactory {

        private final AtomicInteger threadCount = new AtomicInteger();

        private static final AtomicInteger poolCount = new AtomicInteger();

        private final String namePrefix;


        public CustomerThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix + "@pool" + poolCount.incrementAndGet() + '-';
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, nextThreadName());
        }

        private String nextThreadName() {
            return namePrefix + threadCount.incrementAndGet();
        }
    }

    /**
     * Rabbit Connection 生成
     *
     * @return
     */
    public static Connection newRabbitConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setSharedExecutor(new ThreadPoolExecutor(2, 4, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(100),
                new CustomerThreadFactory("rabbit"), new ThreadPoolExecutor.CallerRunsPolicy()));
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("gonnaup.org");
        factory.setVirtualHost("/");
        try {
            return factory.newConnection("named-conn");
        } catch (IOException | TimeoutException e) {
            log.error("获取rabbitmq连接失败");
            throw new RuntimeException(e.getMessage());
        }
    }

    /**************** topic类型Exchange  ****************/
    /**
     * 生产者
     */
    public static void topicExchangeProducer() {
        try (Connection connection = newRabbitConnection(); Channel channel = connection.createChannel()) {
            //声明 Exchange
            channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true);
            String routingKey = "topic.token.jwt";
            int count = 0;
            // 打开生产者的确认模式
            channel.confirmSelect();
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) {
                    log.info("ConfirmListener => {} ack...", deliveryTag);
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) {
                    log.info("{} nack", deliveryTag);
                }
            });
            while (++count < 10) {
                final JwtString jwtString = JwtString.newRandomJWTString();
                //生产端直接将消息发送到 Exchange
                channel.basicPublish(TOPIC_EXCHANGE_NAME, routingKey, null, JsonUtil.toJsonBytes(jwtString));
                log.info("发送消息 => {}", jwtString);
                TimeUnit.SECONDS.sleep(2);
            }
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException | TimeoutException | InterruptedException e) {
            log.error("rabbit client 发送消息发生异常");
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 消费者
     */
    public static void topicExchangeConsumer() {
        try (Connection connection = newRabbitConnection()) {
            Channel channel = connection.createChannel();
            //声明 Exchange，和生产者相同
            channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true);
            final String queueName = "token_all";
            //声明队列，(name, durable, exclusive, autoDelete, arguments)
            channel.queueDeclare(queueName, true, false, false, null);
            //将队列绑定到 Exchange，并指定 routing key
            channel.queueBind(queueName, TOPIC_EXCHANGE_NAME, TOPIC_ROUTING_KEY);
            //消费端最大的消息数量
            channel.basicQos(10);
            String consumerTag = "consumer-1";
            //开始消费
            channel.basicConsume(queueName, false, consumerTag, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    log.info("routingkey {}, contenttype {}, deliverytag {}", envelope.getRoutingKey(), properties.getContentType(), envelope.getDeliveryTag());
                    final JwtString jwtString = JsonUtil.parseToObject(body, JwtString.class);
                    log.info("接收消息 => content => {}", jwtString);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            });
        } catch (IOException e) {
            log.error("rabbit client 消费端出现异常");
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Thread(() -> topicExchangeConsumer()).start();
        topicExchangeProducer();
    }

}
