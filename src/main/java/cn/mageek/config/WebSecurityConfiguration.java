package cn.mageek.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requiresChannel().anyRequest().requiresSecure()
                //通过Spring security将http转到https
                // 但是默认转到了 8443
                .and()
                .formLogin()
                .loginPage("/login")//自定义登录页
                .defaultSuccessUrl("/form")
                .and()
                .logout().logoutSuccessUrl("/login")
                .and()
                .authorizeRequests()
                .antMatchers("/webjars/**", "/login", "/signin/**", "/signup").permitAll()
                .anyRequest().authenticated();

        //自定义https端口
        http
                .portMapper()
                .http(8080).mapsTo(8998);
    }
}
