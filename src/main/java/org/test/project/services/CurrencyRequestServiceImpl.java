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
        return rateExtractor.makeExchange(rawData);
    }

    public Currency requestCurrencyFromSourse(String source) {
//        String responseBody = restClient.getRestClient().get()
//                .uri(
//                        uriBuilder -> uriBuilder
//                                .path("/live")
//                                .queryParam("access_key", accessKey)
//                                .queryParam("source", source)
//                                .build()
//                )
//                .retrieve()
//                .body(String.class);
        String responseBody = "{\n" +
                "    \"success\": true,\n" +
                "    \"terms\": \"https://exchangerate.host/terms\",\n" +
                "    \"privacy\": \"https://exchangerate.host/privacy\",\n" +
                "    \"timestamp\": 1430068515,\n" +
                "    \"source\": \"USD\",\n" +
                "    \"quotes\": {\n" +
                "        \"USDAUD\": 1.278384,\n" +
                "        \"USDCHF\": 0.953975,\n" +
                "        \"USDEUR\": 0.919677,\n" +
                "        \"USDGBP\": 0.658443,\n" +
                "        \"USDPLN\": 3.713873\n" +
                "    }\n" +
                "}";
        return rateExtractor.parseCurrency(source, responseBody);
    }
}
