package cn.mageek.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker//开启使用STOMP协议来传输基于代理的消息，Broker就是代理的意思。
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/111ws").withSockJS();//来注册STOMP协议节点，同时指定使用SockJS协议。 客户端将会链接这个节点
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");//配置消息代理（MessageBroker）
//        registry.setApplicationDestinationPrefixes("/ws");//服务器推送至该通道
    }
}
