package org.hdxy.bangong.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security配置类
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    RestfulAccessDeniedHandler restfulAccessDeniedHandler; //权限不足自定义处理类
    @Autowired
    RestAuthorizationEntryPoint authorizationEntryPoint;  //未登录自定义处理类

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //添加自定义拦截器在用户登录过滤器之前
        http.addFilterBefore(new JwtAuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加授权请求配置
        http.authorizeRequests()
                //将/login", "/logout","/doc.html","/swagger-resources/**","index.html","/webjars/**","/v2/**" 直接通过
                .antMatchers("/login", "/logout","/doc.html","/swagger-resources/**","index.html","/webjars/**","/v2/**").permitAll()
                //静态资源 直接通过
                .antMatchers("/**/**.css","/**/**.js","/**/**.jpg","/**/**.png","/**/.ico").permitAll()
                .anyRequest().authenticated();//其他全部请求需要授权登录
        //添加自定义无权限和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)//无权限
                .authenticationEntryPoint(authorizationEntryPoint);//未登录
        //关闭csrf
        http.csrf().disable();
    }



    //将密码加密的类给Spring管理
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
