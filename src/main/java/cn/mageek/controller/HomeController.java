package cn.mageek.controller;

import cn.mageek.date.CNLocalDateFormatter;
import cn.mageek.pojo.Form;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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

    //暴露一个属性dateFormate 给web页面，整个Controller可见
    //@ModelAttribute注释的方法会在此controller每个方法执行前被执行
    @ModelAttribute("dateFormate")
    public String localFormat(Locale locale){
        //直接进入首页就是 Home:CN；加了 ?lang=cn 或者 ?lang=en 就成了 Home:  ，亦即为空
        //但是 ?lang=cn 或者 ?lang=en 对于messages 的切换是管用的 可能前面那个拦截器影响了这个参数注入吧？
//        System.out.println("Home:"+locale.getCountry());
//        System.out.println("Home:"+locale.toString());
        //上面两行的验证结果就是 有 ?lang=cn 就是 空 和 cn 没有 ?lang=cn 就是 CN 和 zh_CN

//        Locale locale1 = LocaleContextHolder.getLocale();//这样也获取不到
//        System.out.println("Home1:"+locale1.getCountry());

        return CNLocalDateFormatter.getPattern(locale);
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

    @RequestMapping(value = "form",method = RequestMethod.POST)
    public String saveForm(@Valid Form form,//这个form是表单传过来的，同时也能传给表单用于显示
                           BindingResult bindingResult){//校验结果
        if(!bindingResult.hasErrors()){
            System.out.println(form.toString());
        }
        return "form";

    }

}