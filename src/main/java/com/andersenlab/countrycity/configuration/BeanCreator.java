package com.andersenlab.countrycity.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class BeanCreator {
    @Value("${application.image-storage.path}")
    private String imageStoragePath;

    @Bean
    public Path getStorage() {
        Path path = Path.of(imageStoragePath);
        if (Files.notExists(path)) {
            throw new IllegalStateException("Image storage folder not exist");
        }

        return path;
    }
}
