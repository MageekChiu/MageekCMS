package cn.mageek.controller;

import cn.mageek.pojo.Form;
import cn.mageek.pojo.Person;
import cn.mageek.rabbitmq.Sender;
import cn.mageek.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class RestTestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PersonService personService;
    private static AtomicInteger visit = new AtomicInteger(0);
    private final RedisTemplate redisTemplate;
    private final Sender sender;
    @Autowired
    public RestTestController(PersonService personService, RedisTemplate redisTemplate, Sender sender) {
        this.personService = personService;
        this.redisTemplate = redisTemplate;
        this.sender = sender;
    }

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
        logger.debug("esssddsfdfsdfsdfds");
        HttpStatus httpStatus = HttpStatus.OK;
//        httpStatus = HttpStatus.CREATED;//201 CREATED
        return new ResponseEntity<>(new Form(),httpStatus);
    }

    @Transactional
    @RequestMapping(value="/person/{address}")
    public List<Person> getPersonByAddress(@PathVariable String address){
        List<Person> personList = null;
        try {
            personList = personService.findByAddress(address);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return  personList;
    }

    @RequestMapping(value="/redis")
    public String create(){
         /*
            redisTemplate.opsForList();
            redisTemplate.opsForSet();
            redisTemplate.opsForHash()
        */
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("add","asdasdsadsad");
        return valueOperations.get("add");

    }

    @RequestMapping("/send")
    public String send(@RequestParam(name = "msg",defaultValue = "default") String msg ) {
        sender.send(msg);
        return "Send OK.";
    }


}
