package com.example.rev_task_management_project02.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;


@Configuration
@EnableJdbcHttpSession
public class SessionConfig {

    @Bean
    public HttpSessionIdResolver sessionIdResolver(){
        return  HeaderHttpSessionIdResolver.xAuthToken();
    }

}
