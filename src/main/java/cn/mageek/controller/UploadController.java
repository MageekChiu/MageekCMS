package cn.mageek.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.*;

@Controller

public class UploadController {

    private static final Resource picturesDir = new FileSystemResource("./pictures");

    @RequestMapping("upload")
    public String displayUpload(){
        return "uploadImg";
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String uploadImage(MultipartFile file,
    RedirectAttributes ra) throws IOException{
        if (file.isEmpty() || file.getSize()>0.5){
            ra.addFlashAttribute("error","文件大小错误");
            return "redirect:/upload";
        }
        String imgName = file.getOriginalFilename();
        File tempImg = File.createTempFile("pic",getFileExtension(imgName),picturesDir.getFile());
        try(InputStream in = file.getInputStream();
            OutputStream out = new FileOutputStream(tempImg)){
            IOUtils.copy(in,out);
        }
        return "uploadImg";
    }

    private static String getFileExtension(String name){
        return name.substring(name.lastIndexOf("."));
    }
}
