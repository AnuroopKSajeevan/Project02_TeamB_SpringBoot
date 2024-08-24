package com.example.rev_task_management_project02.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    public OpenAPI createopenApi(){
        return new OpenAPI().info(
                new Info().description("Api's for rev_task_management app")
                        .title("RevTaskManagement app")
                        .version("0.1")
        );
    }
}
