package cn.mageek.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SocketController {

    //展示websocket页面
    @RequestMapping("/wss")
    public String ws() {
        return "ws";
    }


    @MessageMapping("/welcome")//和@RequestMapping类似 定义一个路由 客户端向这个频道发送消息
    @SendTo("/topic/getResponse")//表示当服务器有消息需要推送的时候，会对订阅了@SendTo中路径的浏览器发送消息
    public String say(String message) {
        System.out.println(message);
        return "haha"+message;
    }
}
