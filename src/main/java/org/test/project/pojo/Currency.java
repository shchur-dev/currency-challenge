package org.test.project.pojo;

import java.util.Map;
import java.util.Objects;

public sealed class Currency permits CurrencyExchange {
    private final String sourceCurrency;
    private final Map<String, Double> quotes;

    public Currency(String sourceCurrency, Map<String, Double> quotes) {
        this.sourceCurrency = sourceCurrency;
        this.quotes = quotes;
    }

    public String getSourceCurrency() {
        return this.sourceCurrency;
    }

    public Map<String, Double> getQuotes() {
        return this.quotes;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Currency currency = (Currency) object;
        return Objects.equals(sourceCurrency, currency.sourceCurrency) && Objects.equals(quotes, currency.quotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceCurrency, quotes);
    }
}
