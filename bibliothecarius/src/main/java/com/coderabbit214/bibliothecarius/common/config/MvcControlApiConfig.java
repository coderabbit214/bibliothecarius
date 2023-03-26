package com.coderabbit214.bibliothecarius.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Mr_J
 */
@Configuration
@EnableWebMvc
public class MvcControlApiConfig implements WebMvcConfigurer {

    /**
     * Enable CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Set allowed cross-domain routes
        registry.addMapping("/**")
                // Set allowed cross-domain request domain names
                .allowedOriginPatterns("*")
                // Allow credentials (cookies)
                .allowCredentials(true)
                // Set allowed methods
                .allowedMethods("*")
                // CORS allowed time
                .maxAge(3600);
    }

}
