package org.test.project.services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.test.project.pojo.Currency;
import org.test.project.parsing.CurrencyRateExtractor;

@Service
public class CurrencyRequestServiceImpl implements CurrencyRequestService {

    private final CurrencyRateExtractor rateExtractor;

    private final RestClientService clientService;

    public CurrencyRequestServiceImpl(CurrencyRateExtractor rateExtractor, RestClientServiceImpl restClient) {
        this.rateExtractor = rateExtractor;
        this.clientService = restClient;
    }

    @Cacheable({"convert-source", "to"})
    public Currency requestConversionFromSource(String from, String to, String amount) {
        String rawData =  clientService.requestConversion(from, to, amount);
        return rateExtractor.prepareExchangeResult(rawData);
    }

    @Cacheable("source")
    public Currency requestCurrencyFromSource(String source) {
        String responseBody = clientService.getAllExchangeRatesBySource(source);

        return rateExtractor.parseCurrency(source, responseBody);
    }
}
