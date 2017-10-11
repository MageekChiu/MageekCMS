package cn.mageek.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


//@Component
//public class Listener {
//
//    @Value("${mageek.rabbitmq.channel}")
//    private String chanel;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
//
//
//    @RabbitListener(queues = "ajaxMsg11")
//    public void process( String foo) {
//        LOGGER.info("Listener: " + foo);
//    }
//}

@Configuration
//@RabbitListener(queues = "ajaxMsg11")
@RabbitListener(queues = "${mageek.rabbitmq.channel}")
public class Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Bean
    public Queue fooQueue() {
//        return new Queue("ajaxMsg11");
        return new Queue("${mageek.rabbitmq.channel}");
    }

    @RabbitHandler
    public void process(@Payload String foo) {
        LOGGER.info("Listener: " + foo);
    }
}