package cn.mageek.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/")
@Controller
public class HomeController {

    @RequestMapping("/")
    public String hello(Model model){
        model.addAttribute("amessage","message from controller");
        return "index";
    }

    @RequestMapping("haha")
    @ResponseBody
    public String haha(){
        return "haha haha";
    }

}