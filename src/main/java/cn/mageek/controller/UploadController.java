package cn.mageek.controller;

import cn.mageek.config.PictureUploadProperties;
import cn.mageek.util.ExceptionI18Message;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;

@Controller

public class UploadController {

    private final Resource picturesDir;
    private Resource anonymousPic;//用户上传的文件，默认为匿名文件


    @Autowired
    public UploadController(PictureUploadProperties pictureUploadProperties) {
        picturesDir = pictureUploadProperties.getUploadPath();
        anonymousPic = pictureUploadProperties.getAnonymousPicture();
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
    RedirectAttributes ra) throws IOException{
        if (file.isEmpty() || file.getSize()>500000000){
            ra.addFlashAttribute("error", ExceptionI18Message.getLocaleMessage("uploadSizeError") );//传递国际化消息
            return "redirect:/upload";
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

    private static String getFileExtension(String name){
        return name.substring(name.lastIndexOf("."));
    }
}
