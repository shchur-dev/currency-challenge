package org.test.project.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.test.project.pojo.ExchangeUser;

import static statics.Constants.ACCESS_KEY;
import static statics.Constants.AMOUNT_VALUE;
import static statics.Constants.FROM_VALUE;
import static statics.Constants.TO_VALUE;

@Service
public class RestClientServiceImpl implements RestClientService {

    private final RestClient restClient;
    private final ExchangeUser exchangeUser;

    public RestClientServiceImpl(RestClient restClient, ExchangeUser exchangeUser) {
        this.restClient = restClient;
        this.exchangeUser = exchangeUser;
    }

    @Override
    public String requestConversion(String from, String to, String amount) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/convert")
                        .queryParam(ACCESS_KEY, exchangeUser.getAccessKey())
                        .queryParam(FROM_VALUE, from)
                        .queryParam(TO_VALUE, to)
                        .queryParam(AMOUNT_VALUE, amount)
                        .build())
                .retrieve().body(String.class);
    }

    @Override
    public String getAllExchangeRatesBySource(String source) {
        return restClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/live")
                                .queryParam("access_key", exchangeUser.getAccessKey())
                                .queryParam("source", source)
                                .build()
                )
                .retrieve()
                .body(String.class);
    }
}
