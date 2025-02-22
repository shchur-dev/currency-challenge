package org.test.project.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.test.project.exceptions.CurrencyParsingException;
import org.test.project.pojo.Currency;
import org.test.project.pojo.CurrencyExchange;

import java.util.Map;
import java.util.TreeMap;

import static statics.Constants.AMOUNT_VALUE;
import static statics.Constants.FROM_VALUE;
import static statics.Constants.QUERY_NODE;
import static statics.Constants.QUOTES_NODE;
import static statics.Constants.RESULT_VALUE;
import static statics.Constants.TO_VALUE;

@Component
public class CurrencyRateExtractor {

    private final int quoteIndex = 3;
    private final ObjectMapper mapper = new ObjectMapper();

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
            throw new CurrencyParsingException(e.getMessage());
        }
    }

    public Currency prepareExchangeResult(String rawData) {
        try {
            JsonNode root = mapper.readTree(rawData);
            JsonNode queryBody = root.path(QUERY_NODE);
            JsonNode infoBody = root.path("info");
            return new CurrencyExchange(queryBody.get(FROM_VALUE).asText(),
                    Map.of(queryBody.get(TO_VALUE).asText(), infoBody.get("quote").asDouble()),
                    queryBody.get(AMOUNT_VALUE).asDouble(),
                    root.path(RESULT_VALUE).asDouble());

        } catch (RuntimeException | JsonProcessingException e) {
            throw new CurrencyParsingException("Invalid input parameter");
        }
    }
}
