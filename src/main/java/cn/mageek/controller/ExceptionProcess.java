package cn.mageek.controller;

import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionProcess {
    // 对这个异常的统一处理
    @ExceptionHandler(MultipartException.class)
    public ModelAndView onUploadError(HttpServletRequest request,Throwable t) {
        System.out.println("MultipartException:::"+t.getMessage());
        ModelAndView modelAndView = new ModelAndView("uploadImg");
        modelAndView.addObject("error", request.getAttribute(WebUtils.
                ERROR_MESSAGE_ATTRIBUTE));
        return modelAndView;
    }

}
