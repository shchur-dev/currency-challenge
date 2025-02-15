package org.test.project.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.test.project.ProjectApplication;
import org.test.project.pojo.Currency;
import org.test.project.services.CurrencyRequestServiceImpl;
import org.test.project.services.ScheduledCacheCleaner;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static statics.Constants.AMOUNT_VALUE;
import static statics.Constants.FROM_VALUE;
import static statics.Constants.RESULT_VALUE;
import static statics.Constants.TO_VALUE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ProjectApplication.class)
@ActiveProfiles("test")
class CurrencyOpsControllerTest {

    @Autowired
    private CurrencyOpsController currencyOpsController;
    @Autowired
    private ScheduledCacheCleaner scheduledCacheCleaner;
    @MockitoBean
    private CurrencyRequestServiceImpl requestService;

    @AfterEach
    void breakDown() {
        scheduledCacheCleaner.cacheEvict();
    }

    @Test
    void allBySource() {
        Currency expectedCurrency = new Currency("USD", Map.of("AUD", 1.278384, "PLN", 3.713873));

        when(requestService.requestCurrencyFromSourse(any())).thenReturn(expectedCurrency);

        Currency resultCurrency = currencyOpsController.allBySource("USD");

        assertEquals(expectedCurrency, resultCurrency);
    }

    @Test
    void getConversion() {
        var expectedConversionMap = Map.of(AMOUNT_VALUE, "10", FROM_VALUE, "USD", TO_VALUE, "GBP", RESULT_VALUE, "6.58443");
        when(requestService.requestConversionFromSource(any(), any(), any())).thenReturn(expectedConversionMap);

        var actualConversionMap = currencyOpsController.getConversion("USD", "EUR", "10");

        assertEquals(expectedConversionMap, actualConversionMap);
    }

    @Test
    void getExchangeRate() {
        Currency currency = new Currency("USD", Map.of("AUD", 1.278384, "PLN", 3.713873));

        when(requestService.requestCurrencyFromSourse(any())).thenReturn(currency);

        Currency expectedCurrency = new Currency("USD", Map.of("PLN", 3.713873));

        Currency actualCurrency = currencyOpsController.getExchangeRate("USD", "PLN");

        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    void selectedBySource() {
        Currency currency = new Currency("USD", Map.of("AUD", 1.278384, "PLN", 3.713873));

        when(requestService.requestCurrencyFromSourse(any())).thenReturn(currency);

        Currency actualCurrency = currencyOpsController.selectedBySource("USD", List.of("PLN"));

        Currency expectedCurrency = new Currency("USD", Map.of("PLN", 3.713873));

        assertEquals(expectedCurrency, actualCurrency);
    }
}
