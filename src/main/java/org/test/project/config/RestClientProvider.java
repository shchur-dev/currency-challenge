package org.test.project.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.test.project.pojo.exchangeresource.ExchangeHost;

@Configuration
public class RestClientProvider {

    @Bean
    public RestClient createRestClient( @Qualifier("freeExchangeHost") ExchangeHost exchangeHost) {
        return RestClient.builder()
                .baseUrl(exchangeHost.getHost())
                .build();
    }
}
