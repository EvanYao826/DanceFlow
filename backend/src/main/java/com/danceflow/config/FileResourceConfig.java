package com.danceflow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class FileResourceConfig implements WebMvcConfigurer {
    private final String directory;

    public FileResourceConfig(@Value("${danceflow.upload.dir:./uploads}") String directory) {
        this.directory = Paths.get(directory).toAbsolutePath().normalize().toUri().toString();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations(directory);
    }
}
