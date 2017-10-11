package cn.mageek.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import com.google.common.io.Resources;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@ConfigurationProperties(prefix = "upload.pictures")
public class PictureUploadProperties {
    private final ResourceLoader resourceLoader;
    /**
     * 上传路径
     */
    private Resource uploadPath;
    /**
     * 匿名图片路径
     */
    private Resource anonymousPicture;

    @Autowired
    public PictureUploadProperties(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource getAnonymousPicture() {
        return anonymousPicture;
    }

    public void setAnonymousPicture(String anonymousPicture) throws IOException {
        this.anonymousPicture = new DefaultResourceLoader().getResource(anonymousPicture);
        // 因为文件不能打包到 jar中，所以使用这种方式的话，直接还可以，但是打包成jar就会找不到这个属性了。亦即
//        mvnw package
//        java -jar target/mageekcms-0.0.1-SNAPSHOT.jar
//        这两个命令直接使用是不行的
//        Reason: Property 'anonymousPicture' threw exception; nested exception is java.io.FileNotFoundException: class path resource [static/images/pic5687280264637709245.gif] cannot be resolved to absolute file path
//        because it does not reside in the file system: jar:file:/F:/workspace/java/MageekCMS/target/mageekcms-0.0.1-SNAPSHOT.jar!/BOOT-INF/classes!/static/images/pic5687280264637709245.gif
//        但是这个命令可以运行，奇怪
//        mvnw clean install spring-boot:run

//        this.anonymousPicture = resourceLoader.getResource(anonymousPicture);//不行

        if (!this.anonymousPicture.getFile().isFile()) {
            throw new IOException("File " + anonymousPicture + " does not exist");
        }
    }

    public Resource getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) throws IOException {
        this.uploadPath = new DefaultResourceLoader().getResource(uploadPath);
        if (!this.uploadPath.getFile().isDirectory()) {
            throw new IOException("Directory " + uploadPath + " does not exist");
        }
    }
}
