package com.danceflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc/OpenAPI 文档配置。
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI danceFlowOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("DanceFlow API")
                .description("高校街舞社团综合服务平台接口文档")
                .version("v0.1.0"));
    }
}
