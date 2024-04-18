package ziwg.czy_dojade_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static ziwg.czy_dojade_backend.config.SecurityConstraints.FRONTEND_URL;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(FRONTEND_URL)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("Content-Disposition");
    }

//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer){
//        configurer.addPathPrefix("api", HandlerTypePredicate.forAnnotation(RestController.class));
//    }
}
