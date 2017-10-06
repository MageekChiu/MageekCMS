package cn.mageek.controller;

import cn.mageek.pojo.Form;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTestController {

    @RequestMapping("/rest")
    public Form hello(){
        Form form = new Form();
        form.setName("jobs");
        return form;
    }

    @RequestMapping("/restentry")
    public ResponseEntity<Form> restentry(){
        HttpStatus httpStatus = HttpStatus.OK;
        httpStatus = HttpStatus.CREATED;//201 CREATED
        return new ResponseEntity<>(new Form(),httpStatus);
    }
}
