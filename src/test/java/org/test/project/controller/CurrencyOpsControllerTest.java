package org.test.project.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.test.project.ProjectApplication;
import org.test.project.exceptions.CurrencyParsingException;
import org.test.project.pojo.Currency;
import org.test.project.pojo.CurrencyExchange;
import org.test.project.services.CurrencyRequestService;
import org.test.project.services.CurrencyRequestServiceImpl;
import org.test.project.cachehandling.ScheduledCacheCleaner;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

        when(requestService.requestCurrencyFromSource(any())).thenReturn(expectedCurrency);

        ResponseEntity<Currency> resultCurrency = currencyOpsController.allBySource("USD");

        assertEquals(expectedCurrency, resultCurrency.getBody());
    }

    @Test
    void getConversion() {
        Currency expectedExchange = new CurrencyExchange("USD", Map.of("GBP", Double.valueOf("0.65844")), 10.0, 6.58443);
        when(requestService.requestConversionFromSource(any(), any(), any())).thenReturn(expectedExchange);

        ResponseEntity<Currency> actualExchange = currencyOpsController.getConversion("USD", "EUR", "10");

        assertEquals(expectedExchange, actualExchange.getBody());
    }

    @Test
    void getConversionGetsException() {
        when(requestService.requestConversionFromSource(any(), any(), any())).thenThrow(new CurrencyParsingException("Invalid input parameter"));

        String exceptionMessage = assertThrowsExactly(CurrencyParsingException.class,
                () -> currencyOpsController.getConversion("USD", "EUR", "10")).getMessage() ;

        assertEquals("Invalid input parameter", exceptionMessage);
    }

    @Test
    void getExchangeRate() {
        Currency currency = new Currency("USD", Map.of("AUD", 1.278384, "PLN", 3.713873));

        when(requestService.requestCurrencyFromSource(any())).thenReturn(currency);

        Currency expectedCurrency = new Currency("USD", Map.of("PLN", 3.713873));

        ResponseEntity<Currency> actualCurrency = currencyOpsController.getExchangeRate("USD", "PLN");

        assertEquals(expectedCurrency, actualCurrency.getBody());
    }

    @Test
    void selectedBySource() {
        Currency currency = new Currency("USD", Map.of("AUD", 1.278384, "PLN", 3.713873));

        when(requestService.requestCurrencyFromSource(any())).thenReturn(currency);

        ResponseEntity<Currency> actualCurrency = currencyOpsController.selectedBySource("USD", List.of("PLN"));

        Currency expectedCurrency = new Currency("USD", Map.of("PLN", 3.713873));

        assertEquals(expectedCurrency, actualCurrency.getBody());
    }
}
