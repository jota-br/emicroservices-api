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

    // == USER ==
    private String userServiceName;
    private String userServiceUri;
    private String userServicePattern;
    private String userServiceReplacement;

    // == USER H2 ==
    private String userServiceH2Name;
    private String userServiceH2Pattern;
    private String userServiceH2Replacement;

    // == USER SWAGGER
    private String userServiceSwaggerName;
    private String userServiceSwaggerPattern;
    private String userServiceSwaggerReplacement;

    private String userServiceSwaggerApiDocsName;
    private String userServiceSwaggerApiDocsPattern;
    private String userServiceSwaggerApiDocsReplacement;

    // == USER ACTIVATION ==
    private String userActivationServiceName;
    private String userActivationServicePattern;
    private String userActivationServiceReplacement;

    // == INVENTORY ==
    private String inventoryServiceName;
    private String inventoryServiceUri;
    private String inventoryServicePattern;
    private String inventoryServiceReplacement;

    // == INVENTORY SWAGGER ==
    private String inventoryServiceSwaggerName;
    private String inventoryServiceSwaggerPattern;
    private String inventoryServiceSwaggerReplacement;

    private String inventoryServiceSwaggerApiDocsName;
    private String inventoryServiceSwaggerApiDocsPattern;
    private String inventoryServiceSwaggerApiDocsReplacement;

    // == ORDER ==
    private String orderServiceName;
    private String orderServiceUri;
    private String orderServicePattern;
    private String orderServiceReplacement;

    // == ORDER SWAGGER ==
    private String orderServiceSwaggerName;
    private String orderServiceSwaggerPattern;
    private String orderServiceSwaggerReplacement;

    private String orderServiceSwaggerApiDocsName;
    private String orderServiceSwaggerApiDocsPattern;
    private String orderServiceSwaggerApiDocsReplacement;

    // == PRODUCT ==
    private String productServiceName;
    private String productServiceUri;
    private String productServicePattern;
    private String productServiceReplacement;

    // == PRODUCT SWAGGER ==
    private String productServiceSwaggerName;
    private String productServiceSwaggerPattern;
    private String productServiceSwaggerReplacement;

    private String productServiceSwaggerApiDocsName;
    private String productServiceSwaggerApiDocsPattern;
    private String productServiceSwaggerApiDocsReplacement;
}

