package com.food.ordering.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "order-query-service")
public class OrderQueryServiceConfigData {
    private String version;
    private String customAudience;
    private Long backPressureDelayMs;
    private WebClient webClient;
    private Query query;
    @Data
    public static class WebClient {
        private Integer connectTimeoutMs;
        private Integer readTimeoutMs;
        private Integer writeTimeoutMs;
        private Integer maxInMemorySize;
        private String contentType;
        private String acceptType;
        private String queryType;
    }

    @Data
    public static class Query {
        private String method;
        private String accept;
        private String uri;
    }
}
