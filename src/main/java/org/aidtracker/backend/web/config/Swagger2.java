package org.aidtracker.backend.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

/**
 * @author MT
 * @version 0.1
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    @Bean
    public Docket createRestApi() {
        Parameter parameter = new ParameterBuilder()
                .name(JwtAuthTokenFilter.TOKEN_HEADER_KEY)
                .description("token 对于非 public, auth 下接口必需")
                .modelRef(new ModelRef("String"))
                .parameterType("header")
                .required(false)
                .build();

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(Collections.singletonList(parameter))
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("AidTracker API")
                .description("A backend project that provides api services for aid tracker wechat mini program and web page\n" +
                        "文档：https://shimo.im/docs/8wgRQpryKCPQRqJy/\n" +
                        "<a href=\"https://github.com/majiaxin110/aid-tracker-backend\">Github Repository</a>")
                .version("0.0.1")
                .build();
    }
}
