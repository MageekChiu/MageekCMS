package cn.mageek.controller;

import cn.mageek.date.CNLocalDateFormatter;
import cn.mageek.pojo.Form;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@RequestMapping("/")
@Controller
public class HomeController {

    @RequestMapping("/")
    public String hello(@RequestParam(name = "name",defaultValue = "default") String name,
                        Model model){
        model.addAttribute("message","message from controller,hello "+name);
        return "index";
    }

    @RequestMapping("haha")
    @ResponseBody
    public String haha(){
        return "haha haha";
    }

//    @RequestMapping("form")
//    public ModelAndView displayForm(){
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("form");
//        modelAndView.addObject("form",new Form());
//        return modelAndView;
//    }

    //下面这种写法与上面等价
    @RequestMapping("form")
    public String displayForm(Form form){
        return "form";
    }

    //暴露一个属性dateFormate 给web页面，整个Controller可见
    @ModelAttribute("dateFormate")
    public String localFormat(Locale locale){
        return CNLocalDateFormatter.getPattern(locale);
    }

    @RequestMapping(value = "form",method = RequestMethod.POST)
    public String saveForm(Form form){
        System.out.println(form.toString());
        return "form";
    }

}