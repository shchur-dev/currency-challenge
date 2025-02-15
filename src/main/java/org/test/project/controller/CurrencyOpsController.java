package org.test.project.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.test.project.pojo.Currency;
import org.test.project.services.CurrencyRequestServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static statics.Constants.AMOUNT_VALUE;
import static statics.Constants.BASE_CACHE;
import static statics.Constants.CONVERSION_CACHE;
import static statics.Constants.FROM_VALUE;
import static statics.Constants.RESULT_VALUE;
import static statics.Constants.SOURCE_VALUE;
import static statics.Constants.TO_VALUE;

@RestController
public class CurrencyOpsController {

    private final CacheManager cacheManager;
    private final CurrencyRequestServiceImpl requestService;


    public CurrencyOpsController(CacheManager cacheManager, CurrencyRequestServiceImpl requestService) {
        this.cacheManager = cacheManager;
        this.requestService = requestService;
    }

    @Cacheable(BASE_CACHE)
    @GetMapping("/currency")
    public Currency allBySource(@PathParam(SOURCE_VALUE) String source) {
        return requestService.requestCurrencyFromSourse(source);
    }

    @Cacheable(CONVERSION_CACHE)
    @GetMapping("/convert")
    public Map<String, ?> getConversion(@PathParam(FROM_VALUE) String from, @PathParam(TO_VALUE) String to, @PathParam(AMOUNT_VALUE) String amount) {
        if (Objects.requireNonNull(cacheManager.getCache(BASE_CACHE)).retrieve(from) != null && Objects.nonNull(to)) {
            Currency resultFromCache = cacheManager.getCache(BASE_CACHE).get(from, Currency.class);
            return Map.of(FROM_VALUE, resultFromCache.sourceCurrency(),
                    TO_VALUE, to,
                    AMOUNT_VALUE, amount,
                    RESULT_VALUE, Double.parseDouble(amount) * resultFromCache.quotes().getOrDefault(to, Double.NaN));
        } else {
            return requestService.requestConversionFromSource(from, to, amount);
        }
    }

    @GetMapping("/rate")
    public Currency getExchangeRate(@PathParam(FROM_VALUE) String from, @PathParam(TO_VALUE) String to) {

        if (Objects.requireNonNull(cacheManager.getCache(BASE_CACHE)).retrieve(from) != null && Objects.nonNull(to)) {
            Currency resultFromCache = cacheManager.getCache(BASE_CACHE).get(from, Currency.class);
            return new Currency(from, Map.of(to, resultFromCache.quotes().getOrDefault(to, Double.NaN)));
        } else {
            Currency fromSourse = requestService.requestCurrencyFromSourse(from);
            return new Currency(fromSourse.sourceCurrency(), Map.of(to, fromSourse.quotes().getOrDefault(to, Double.NaN)));
        }
    }

    @Cacheable(BASE_CACHE)
    @PutMapping("/rates")
    public Currency selectedBySource(@PathParam(SOURCE_VALUE) String source, @RequestBody List<String> quotesList) {
        Map<String, Double> quotesByBase = requestService.requestCurrencyFromSourse(source).quotes();
        Map<String, Double> result = new TreeMap<>();
        quotesList.forEach(quote -> result.put(quote, quotesByBase.get(quote)));
        return new Currency(source, result);
    }
}
