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
//    @Autowired
//    UserDetailsServiceImpl userDetailsService;
    @Autowired
    RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    RestAuthorizationEntryPoint authorizationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //添加自定义拦截器
        http.addFilterBefore(new JwtAuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.formLogin();
        http.authorizeRequests()
                .antMatchers("/login", "/logout").permitAll()
                .anyRequest().authenticated();
        //添加自定义未授权和为登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(authorizationEntryPoint);
        http.csrf().disable();
    }



    //将密码加密的类给Spring管理
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
