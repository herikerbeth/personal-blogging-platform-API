package blog;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://personal-blogging-platform-api.up.railway.app/",
                        "http://localhost:8080/")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info().version("1.0.0")
                        .title("Personal Blogging Platform API")
                        .description("This is a RESTful API for a personal blogging platform. It allows users to " +
                                "create, read, update, and delete blog posts.")
        );
    }
}
