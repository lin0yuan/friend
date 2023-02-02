package com.ayit.friend.config;


import com.ayit.friend.handler.CustomAuthenticationFailureHandler;
import com.ayit.friend.handler.CustomLoginSuccessHandler;
import com.ayit.friend.handler.CustomLogoutSuccessHandler;
import com.ayit.friend.handler.CustomOAuth2SuccessHandler;
import com.ayit.friend.service.impl.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsService userService;

    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private CustomLoginSuccessHandler customLoginSuccessHandler;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/register/**","/ws/**");
        web.ignoring().antMatchers("/swagger/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/v2/**")
                .antMatchers("/v3/**")
                .antMatchers("/v2/api-docs-ext/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/doc.html")
                .antMatchers("/webjars/**")
                .antMatchers("/login.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用 CSRF
        http.authorizeRequests()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .usernameParameter("emailAddress")
                    .passwordParameter("password")// 关闭csrf
                    .loginProcessingUrl("/user/login")
                    .successHandler(customLoginSuccessHandler)
                    .failureHandler(customAuthenticationFailureHandler)
                    .permitAll()
                .and()
                .logout()
                    .logoutUrl("/user/logout")
                    .logoutSuccessHandler(customLogoutSuccessHandler)
                    .permitAll()
                .and()
                .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .userDetailsService(userService)
                .and()// 再次获取到HttpSecurity对象
                .userDetailsService(userService)
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint((req,resp,exception)-> {
                    resp.setStatus(401);
                });
        http.oauth2Login()
                .successHandler(customOAuth2SuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .userInfoEndpoint()
                .userService(new CustomOAuth2UserService());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        tokenRepository.setCreateTableOnStartup(false);
        return tokenRepository;
    }
}


