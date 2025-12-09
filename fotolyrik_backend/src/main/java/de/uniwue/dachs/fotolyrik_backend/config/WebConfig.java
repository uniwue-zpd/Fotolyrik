package de.uniwue.dachs.fotolyrik_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${config.files.upload-dir}")
    private String uploadDirValue;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = ResourceUtils.FILE_URL_PREFIX;
        if (uploadDirValue.endsWith("/")) {
            location += uploadDirValue;
        } else {
            location += uploadDirValue + "/";
        }

        registry
            .addResourceHandler("/uploads/**")
            .addResourceLocations(location);
    }
}
