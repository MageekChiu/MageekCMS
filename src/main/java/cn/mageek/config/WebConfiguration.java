package cn.mageek.config;

import cn.mageek.date.CNLocalDateFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebAppRootListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.time.LocalDate;
import java.util.Locale;

@Configuration
@EnableConfigurationProperties({PictureUploadProperties.class})//自定义属性
//@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />，但在boot中 无需显示开启
public class WebConfiguration extends WebMvcConfigurerAdapter implements ServletContextInitializer {

    @Override
//    添加WebAppRootListener监听器
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(WebAppRootListener.class);
        servletContext.setInitParameter("webAppRootKey", "mc.root");
    }

    /* 日期格式化、国际化问题 */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(LocalDate.class, new CNLocalDateFormatter());
    }

    /* 语言拦截切换 */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.CHINA);//默认中国
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /* 统一处理 MultipartException */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> container.addErrorPages(new ErrorPage(MultipartException.class, "/uploadError"));
    }

//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setMaxFileSize("128KB");
//        factory.setMaxRequestSize("128KB");
//        return factory.createMultipartConfig();
//    }

    /* Jackson的日期问题 这样才不会显示成时间戳*/
    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    // 首先使用注解 @EnableTransactionManagement 开启事务支持后，然后在访问数据库的Service方法上添加注解 @Transactional 便可。
    // 关于事务管理器，不管是JPA还是JDBC等都实现自接口 PlatformTransactionManager 如果你添加的是 spring-boot-starter-jdbc 依赖，
    // 框架会默认注入 DataSourceTransactionManager 实例。如果你添加的是 spring-boot-starter-data-jpa 依赖，框架会默认注入
    // JpaTransactionManager 实例。
    // 这些SpringBoot为我们自动做了，这些对我们并不透明，如果你项目做的比较大，添加的持久化依赖比较多，我们还是会选择人为的
    // 指定使用哪个事务管理器。
    // 其中 dataSource 框架会自动为我们注入
    // 在Spring容器中，我们手工注解@Bean 将被优先加载，框架不会重新实例化其他的 PlatformTransactionManager 实现类。
    // 然后在Service中，被 @Transactional 注解的方法，将支持事务。如果注解在类上，则整个类的所有方法都默认支持事务。
    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }





    /* 中文乱码问题 */
//    //原来不是乱码问题 而是新建的这个 message_cn 居然是gb2312 要把idea文件默认格式改为utf8才行
//    //File->Other Settings->Default Settings->Editor->File Encodings ，这儿设置的是默认的文件编码方式，所有新建的工程使用的都是默认的文件编码方式。
//    @Bean
//    public HttpMessageConverter<String> responseBodyConverter() {
//        StringHttpMessageConverter converter = new StringHttpMessageConverter(
//                Charset.forName("UTF-8"));
//        return converter;
//    }
//
//    @Override
//    public void configureMessageConverters(
//            List<HttpMessageConverter<?>> converters) {
//        super.configureMessageConverters(converters);
//        converters.add(responseBodyConverter());
//    }
//
//    @Override
//    public void configureContentNegotiation(
//            ContentNegotiationConfigurer configurer) {
//        configurer.favorPathExtension(false);
//    }

}