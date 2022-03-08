package com.windstoss.messanger.api.config;


import com.windstoss.messanger.utils.MessageTypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/files/**")
                .addResourceLocations("file:/home/windstoss/Documents/msgrDownloads");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MessageTypeConverter());
    }
}
