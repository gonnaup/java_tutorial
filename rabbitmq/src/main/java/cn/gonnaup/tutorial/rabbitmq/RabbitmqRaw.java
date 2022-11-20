package cn.gonnaup.tutorial.rabbitmq;

import cn.gonnaup.tutorial.common.domain.JwtString;
import cn.gonnaup.tutorial.common.util.JsonUtil;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gonnaup
 * @version created at 2022/11/19 下午2:00
 */
@Slf4j
public class RabbitmqRaw {

    //'#'匹配0～n个单词，'*'匹配一个单词
    static final String TOPIC_ROUTING_KEY = "topic.token.*";

    static final String TOPIC_EXCHANGE_NAME = "exchange_token";

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

    public static Connection newRabbitConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setSharedExecutor(new ThreadPoolExecutor(2, 4, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(100),
                new CustomerThreadFactory("rabbit"), new ThreadPoolExecutor.CallerRunsPolicy()));
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("localhost");
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
            while (++count < 100) {
                final JwtString jwtString = JwtString.randomJWTString();
                //生产端直接将消息发送到 Exchange
                channel.basicPublish(TOPIC_EXCHANGE_NAME, routingKey, null, JsonUtil.toJsonBytes(jwtString));
                log.info("发送消息 => {}", jwtString);
                TimeUnit.SECONDS.sleep(2);
            }
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消费者
     */
    public static void topicExchangeConsumer() {
        try {
            Connection connection = newRabbitConnection();
            Channel channel = connection.createChannel();
            //声明 Exchange，和生产者相同
            channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true);
            final String queueName = "token_all";
            //声明队列，(name, durable, exclusive, autoDelete, arguments)
            channel.queueDeclare(queueName, true, false, false, null);
            //将队列绑定到 Exchange，并指定 routing key
            channel.queueBind(queueName, TOPIC_EXCHANGE_NAME, TOPIC_ROUTING_KEY);
            String consumerTag = "consumer-1";
            //开始消费
            channel.basicConsume(queueName, false, consumerTag, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    log.info("routingkey {}, contenttype {}, deliverytag {}", envelope.getRoutingKey(), properties.getContentType(), envelope.getDeliveryTag());
                    final JwtString jwtString = JsonUtil.parseToObject(body, JwtString.class);
                    log.info("content => {}", jwtString);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> topicExchangeConsumer()).start();
        topicExchangeProducer();
    }

}
