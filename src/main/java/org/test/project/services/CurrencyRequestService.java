package org.test.project.services;

import org.test.project.pojo.Currency;

import java.util.Map;

public interface CurrencyRequestService {
    Map<String, ?> requestConversionFromSource(String from, String to, String amount);
    Currency requestCurrencyFromSourse(String source);
}
