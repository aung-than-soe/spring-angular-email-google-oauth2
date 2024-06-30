package org.codingtest.config;

import com.google.api.client.http.HttpMethods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.security.oauth2.resource-server.opaque-token.introspection-uri}")
    public String introspectURI;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods(HttpMethods.GET, HttpMethods.POST, HttpMethods.OPTIONS, HttpMethods.PUT, HttpMethods.PATCH, HttpMethods.DELETE)
                .allowedOrigins("http://localhost:4200");
    }

    @Bean
    public WebClient webClient() {
       return WebClient.builder().baseUrl(introspectURI).build();
    }
}
