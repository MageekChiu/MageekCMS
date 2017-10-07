package cn.mageek.controller;

import cn.mageek.config.PictureUploadProperties;
import cn.mageek.util.ExceptionI18Message;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.Locale;

@Controller
public class UploadController {

    private final Resource picturesDir;
    private Resource anonymousPic;//用户上传的文件，默认为匿名文件

    private final MessageSource messageSource;

    @Autowired
    public UploadController(PictureUploadProperties pictureUploadProperties,MessageSource messageSource) {
        picturesDir = pictureUploadProperties.getUploadPath();
        anonymousPic = pictureUploadProperties.getAnonymousPicture();
        this.messageSource = messageSource;
    }

    @ModelAttribute("picturePath")
    public Resource picturePath(){
        return anonymousPic;
    }

    @RequestMapping("upload")
    public String displayUpload(){
        return "uploadImg";
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String uploadImage(MultipartFile file,
                              RedirectAttributes ra, Locale locale) throws IOException{
        if (file.isEmpty() || file.getSize()>50000){
//            ra.addFlashAttribute("error", ExceptionI18Message.getLocaleMessage("uploadSizeError") );//传递国际化消息
//            ra.addFlashAttribute("error", messageSource.getMessage("uploadSizeError",null,locale) );//也可以传递国际化消息
//            return "redirect:/upload";
            throw new IOException(messageSource.getMessage("uploadSizeError",null,locale));//也可以把消息交出去统一处理
        }
        String imgName = file.getOriginalFilename();
        File tempImg = File.createTempFile("pic",getFileExtension(imgName),picturesDir.getFile());
        try(InputStream in = file.getInputStream();
            OutputStream out = new FileOutputStream(tempImg)){
            IOUtils.copy(in,out);
        }
        anonymousPic = new FileSystemResource(tempImg);//修改用户上传的文件
        return "uploadImg";
    }

    @RequestMapping("picture")
    public void getUploadedPicture(HttpServletResponse response,
                                   @ModelAttribute("picturePath") Resource picturePath  ) throws IOException {
//        ClassPathResource classPathResource = new ClassPathResource("/images/pic5687280264637709245.gif");//写死

//        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(anonymousPic.getFilename()));//写在配置里面

        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.getFilename()));//默认写在配置里面，但是根据用户上传进行变化
        IOUtils.copy(picturePath.getInputStream(), response.getOutputStream());

    }

    //统一处理该控制器的IOException
    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(IOException exception){
        ModelAndView modelAndView = new ModelAndView("uploadImg");
        modelAndView.addObject("error",exception.getMessage());
        return modelAndView;
    }

    //统一处理 multipart 错误
    // 要注意 手动设置spring.http.multipart.max-file-size=1KB 然后传一个500KB的文件 这个方法管用
    // 设为5MB选一个7MB的也行，但是选一个11M的就不行了，更别说几个G的
    // 设为40 选一个 47的也不行 ，看样子是传了2次
    // 设为40 可以传39的，所以这个值还是设大一点好
    // 设为40 传48的 使用 ControllerAdvice 拦截测试可以清楚的看见onUploadError出现了两次，MultipartException:::也出现了两次 紧随其后 所以的确都拦截了两次
    // 答案  Handling MultipartException with Spring Boot and display error page  https://stackoverflow.com/questions/29363705/handling-multipartexception-with-spring-boot-and-display-error-page
    @RequestMapping("uploadError")
    public ModelAndView onUploadError(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("uploadImg");
        modelAndView.addObject("error", request.getAttribute(WebUtils.
                ERROR_MESSAGE_ATTRIBUTE));
        return modelAndView;
    }

    private static String getFileExtension(String name){
        return name.substring(name.lastIndexOf("."));
    }
}
