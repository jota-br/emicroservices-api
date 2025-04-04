package io.github.jotabrc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "service")
@PropertySource("classpath:service.properties")
public class ServiceConfiguration {

    private String userServiceName;
    private String userServiceUri;
    private String userServicePattern;
    private String userServiceReplacement;

    private String inventoryServiceName;
    private String inventoryServiceUri;
    private String inventoryServicePattern;
    private String inventoryServiceReplacement;

    private String orderServiceName;
    private String orderServiceUri;
    private String orderServicePattern;
    private String orderServiceReplacement;

    private String productServiceName;
    private String productServiceUri;
    private String productServicePattern;
    private String productServiceReplacement;
}

