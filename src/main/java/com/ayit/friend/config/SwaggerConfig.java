package com.ayit.friend.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
public class SwaggerConfig {

    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                    .title("friend")
                .contact(new Contact("linq","http:localhost:8080/doc.html","3102691553@qq.com"))
                    .version("2.0")
                    .description("校园friend")
                    .build();
    }
    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */


    @Bean
    public Docket docket(){
        return  new Docket(DocumentationType.OAS_30)
//        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .apiInfo(apiInfo())
                .groupName("Friend Api")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ayit.friend"))
                .paths(PathSelectors.any())
                .build();
    }
}
