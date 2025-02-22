package org.test.project.services;

import org.test.project.pojo.Currency;

public interface CurrencyRequestService {

    Currency requestConversionFromSource(String from, String to, String amount);
    Currency requestCurrencyFromSource(String source);
}
