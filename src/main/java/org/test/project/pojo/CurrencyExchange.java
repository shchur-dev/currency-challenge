package org.test.project.pojo;

import java.util.Map;
import java.util.Objects;

public final class CurrencyExchange extends Currency {

    private final Double amount;
    private final Double exchangeResult;

    public CurrencyExchange(String sourceCurrency, Map<String, Double> quotes, Double amount, Double exchangeResult) {
        super(sourceCurrency, quotes);
        this.amount = amount;
        this.exchangeResult = exchangeResult;
    }


    public Double getAmount() {
        return this.amount;
    }

    public Double getExchangeResult() {
        return this.exchangeResult;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        CurrencyExchange exchange = (CurrencyExchange) object;
        return Objects.equals(getAmount(), exchange.getAmount()) && Objects.equals(getExchangeResult(), exchange.getExchangeResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAmount(), getExchangeResult());
    }
}
