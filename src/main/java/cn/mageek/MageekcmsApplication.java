package cn.mageek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

//暂时不配置数据源 所以必须exclude1,2
//3是为了暂时关闭security
//也可以在 application.properties 中
// security.basic.enabled=false
@SpringBootApplication(exclude={
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
//        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
})
public class MageekcmsApplication {
	public static void main(String[] args) {
		SpringApplication.run(MageekcmsApplication.class, args);
	}
}
