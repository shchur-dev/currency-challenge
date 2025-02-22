package org.test.project.parsing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.test.project.exceptions.CurrencyParsingException;
import org.test.project.pojo.Currency;
import org.test.project.pojo.CurrencyExchange;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyRateExtractorTest{

    @Autowired
    private CurrencyRateExtractor extractor;

    @Test
    void parseCurrencySuccessCase() {
        Currency actualCurrency = extractor.parseCurrency("USD", getRawQuotesByBase());

        Currency expectedCurrency = new Currency("USD", Map.of("AUD", 1.278384, "PLN", 3.713873));

        assertEquals(expectedCurrency.getSourceCurrency(), actualCurrency.getSourceCurrency());
        assertEquals(expectedCurrency.getQuotes(), actualCurrency.getQuotes());
    }

    @Test
    void makeExchangeSuccessCase() {

        Currency actualExchangeResult = extractor.prepareExchangeResult(getRawConversionResult());

        CurrencyExchange expectedExchangeResult = new CurrencyExchange("USD", Map.of("GBP", Double.valueOf("0.658443")), 10.0, 6.58443);

        assertEquals(expectedExchangeResult.getSourceCurrency(), actualExchangeResult.getSourceCurrency());
        assertEquals(expectedExchangeResult.getQuotes(), actualExchangeResult.getQuotes());
        assertEquals(expectedExchangeResult.getAmount(), ((CurrencyExchange) actualExchangeResult).getAmount());
        assertEquals(expectedExchangeResult.getExchangeResult(), ((CurrencyExchange) actualExchangeResult).getExchangeResult());
    }

    @Test
    void makeExchangeFailureCase() {
        CurrencyParsingException exception = assertThrowsExactly(CurrencyParsingException.class, () -> extractor.prepareExchangeResult(null));

        assertEquals("Invalid input parameter", exception.getMessage());
        assertEquals(CurrencyParsingException.class, exception.getClass());
    }

    private static String getRawConversionResult() {
        return  "{\n" +
                "    \"success\": true,\n" +
                "    \"terms\": \"https://exchangerate.host/terms\",\n" +
                "    \"privacy\": \"https://exchangerate.host/privacy\",\n" +
                "    \"query\": {\n" +
                "        \"from\": \"USD\",\n" +
                "        \"to\": \"GBP\",\n" +
                "        \"amount\": 10\n" +
                "    },\n" +
                "    \"info\": {\n" +
                "        \"timestamp\": 1430068515,\n" +
                "        \"quote\": 0.658443\n" +
                "    },\n" +
                "    \"result\": 6.58443\n" +
                "}";
    }

    private static String getRawQuotesByBase() {
        return "{\n" +
                "    \"success\": true,\n" +
                "    \"terms\": \"https://exchangerate.host/terms\",\n" +
                "    \"privacy\": \"https://exchangerate.host/privacy\",\n" +
                "    \"timestamp\": 1430068515,\n" +
                "    \"source\": \"USD\",\n" +
                "    \"quotes\": {\n" +
                "        \"USDAUD\": 1.278384,\n" +
                "        \"USDPLN\": 3.713873\n" +
                "    }\n" +
                "}";
    }
}
