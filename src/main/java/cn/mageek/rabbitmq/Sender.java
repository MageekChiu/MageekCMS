package cn.mageek.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    @Value("${mageek.rabbitmq.channel}")
//    private String chanel = "ajaxMsg11";
    private String chanel ;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String msg) {
        this.rabbitTemplate.convertAndSend(chanel, msg);
    }
}
