package org.test.project.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.test.project.config.RestClientProvider;
import org.test.project.pojo.Currency;
import org.test.project.parsing.CurrencyRateExtractor;

import java.util.Map;

import static statics.Constants.ACCESS_KEY;
import static statics.Constants.AMOUNT_VALUE;
import static statics.Constants.FROM_VALUE;
import static statics.Constants.TO_VALUE;

@Service
public class CurrencyRequestServiceImpl implements CurrencyRequestService {
    @Value("${access_key}")
    private String accessKey;

    private final CurrencyRateExtractor rateExtractor;
    private final RestClientProvider restClient;

    public CurrencyRequestServiceImpl(CurrencyRateExtractor rateExtractor, RestClientProvider restClient) {
        this.rateExtractor = rateExtractor;
        this.restClient = restClient;
    }

    public Map<String, String> requestConversionFromSource(String from, String to, String amount) {
        String rawData =  restClient.getRestClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/convert")
                        .queryParam(ACCESS_KEY, accessKey)
                        .queryParam(FROM_VALUE, from)
                        .queryParam(TO_VALUE, to)
                        .queryParam(AMOUNT_VALUE, amount)
                        .build())
                .retrieve().body(String.class);
        return rateExtractor.prepareExchangeResult(rawData);
    }

    public Currency requestCurrencyFromSourse(String source) {
        String responseBody = restClient.getRestClient().get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/live")
                                .queryParam("access_key", accessKey)
                                .queryParam("source", source)
                                .build()
                )
                .retrieve()
                .body(String.class);
        return rateExtractor.parseCurrency(source, responseBody);
    }
}
