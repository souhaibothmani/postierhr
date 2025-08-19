package com.example.postierhr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("public/accueil");
        registry.addViewController("/connexion").setViewName("public/connexion");
        registry.addViewController("/inscription").setViewName("public/inscription");
    }
}