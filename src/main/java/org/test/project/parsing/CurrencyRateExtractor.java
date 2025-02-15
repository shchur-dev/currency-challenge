package org.test.project.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import org.test.project.pojo.Currency;

import java.util.Map;
import java.util.TreeMap;

import static statics.Constants.AMOUNT_VALUE;
import static statics.Constants.BASE_CACHE;
import static statics.Constants.FROM_VALUE;
import static statics.Constants.QUERY_NODE;
import static statics.Constants.QUOTES_NODE;
import static statics.Constants.RESULT_VALUE;
import static statics.Constants.TO_VALUE;

@Component
public class CurrencyRateExtractor {

    private final int quoteIndex = 3;
    private final ObjectMapper mapper = new ObjectMapper();

    @CachePut(BASE_CACHE)
    public Currency parseCurrency(String source, String rawData) {
        try {
            JsonNode root = mapper.readTree(rawData);
            Map<String, Double> values = new TreeMap<>();
            root.path(QUOTES_NODE)
                    .properties().forEach(set -> {
                        values.put(set.getKey().substring(quoteIndex), set.getValue().doubleValue());
                    });

            return new Currency(source, values);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> makeExchange(String rawData) {
        try {
            JsonNode root = mapper.readTree(rawData);
            JsonNode queryBody = root.path(QUERY_NODE);
            return Map.of(FROM_VALUE, queryBody.get(FROM_VALUE).asText(),
                    TO_VALUE, queryBody.get(TO_VALUE).asText(),
                    AMOUNT_VALUE, queryBody.get(AMOUNT_VALUE).asText(),
                    RESULT_VALUE, String.valueOf(root.path(RESULT_VALUE)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
