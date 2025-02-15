package org.test.project.parsing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.test.project.pojo.Currency;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static statics.Constants.AMOUNT_VALUE;
import static statics.Constants.FROM_VALUE;
import static statics.Constants.RESULT_VALUE;
import static statics.Constants.TO_VALUE;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CurrencyRateExtractorTest{

    @Autowired
    private CurrencyRateExtractor extractor;

    @Test
    void parseCurrencySuccessCase() {
        final String rawResponse ="{\n" +
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
        Currency actualCurrency = extractor.parseCurrency("USD", rawResponse);

        Currency expectedCurrency = new Currency("USD", Map.of("AUD", 1.278384, "PLN", 3.713873));

        assertEquals(expectedCurrency, actualCurrency);
    }

    @Test
    void makeExchangeSuccessCase() {
        String requestResultRawData = "{\n" +
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
        var actualExchangeResult = extractor.makeExchange(requestResultRawData);

        var expectedResult = Map.of(AMOUNT_VALUE, "10", FROM_VALUE, "USD", TO_VALUE, "GBP", RESULT_VALUE, "6.58443");

        assertEquals(expectedResult.get(FROM_VALUE), actualExchangeResult.get(FROM_VALUE));
        assertEquals(expectedResult.get(TO_VALUE), actualExchangeResult.get(TO_VALUE));
        assertEquals(expectedResult.get(RESULT_VALUE), actualExchangeResult.get(RESULT_VALUE));
        assertEquals(expectedResult.get(AMOUNT_VALUE), actualExchangeResult.get(AMOUNT_VALUE));
    }
}
