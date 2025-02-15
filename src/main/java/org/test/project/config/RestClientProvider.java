package org.test.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import static statics.Constants.HOST_URL;

@Configuration
public class RestClientProvider {

    @Bean
    public RestClient getRestClient() {
        return RestClient.builder()
                .baseUrl(HOST_URL)
                .build();
    }
}
