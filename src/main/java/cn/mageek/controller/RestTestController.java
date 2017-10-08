package cn.mageek.controller;

import cn.mageek.pojo.Form;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class RestTestController {



    private static AtomicInteger visit = new AtomicInteger(0);
    @RequestMapping("/rest")
    @Cacheable("searches")
    public Form hello(){
        Form form = new Form();
        form.setName("jobs");
        form.setEmail("12121@qq.com");
        form.setHobbies(Arrays.asList("特殊", "212", "奥迪"));
        form.setBirthDate(LocalDate.now());
        try {
            Thread.sleep(5000);//模拟耗时操作，只有第一次才会等5秒以上才有结果，后面都是马上就有了，说明缓存起作用了
            System.out.println(visit+" 次访问");
            visit.addAndGet(1);//加一
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return form;
    }

    @RequestMapping("/restentry")
    public ResponseEntity<Form> restentry(){
        HttpStatus httpStatus = HttpStatus.OK;
        httpStatus = HttpStatus.CREATED;//201 CREATED
        return new ResponseEntity<>(new Form(),httpStatus);
    }


}
