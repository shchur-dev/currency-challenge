package org.test.project.services;

public interface RestClientService {
    String requestConversion(String from, String to, String amount);

    String getAllExchangeRatesBySource(String source);
}
