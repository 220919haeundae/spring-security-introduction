package com.example.TestSecurity.config;

import com.example.TestSecurity.aop.LoggingAOP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig implements WebMvcConfigurer {

//    @Bean
//    public LoggingAOP myAspect() {
//        return new LoggingAOP();
//    }
}
