package org.test.project.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.test.project.pojo.Currency;
import org.test.project.services.CurrencyRequestService;
import org.test.project.services.CurrencyRequestServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static statics.Constants.AMOUNT_VALUE;
import static statics.Constants.FROM_VALUE;
import static statics.Constants.SOURCE_VALUE;
import static statics.Constants.TO_VALUE;

@RestController
public class CurrencyOpsController {

    private final CurrencyRequestService requestService;

    public CurrencyOpsController(CurrencyRequestServiceImpl requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/currency")
    public ResponseEntity<Currency> allBySource(@PathParam(SOURCE_VALUE) String source) {
        return ResponseEntity.ok(requestService.requestCurrencyFromSource(source));
    }

    @GetMapping("/convert")
    public ResponseEntity<Currency> getConversion(@PathParam(FROM_VALUE) String from, @PathParam(TO_VALUE) String to, @PathParam(AMOUNT_VALUE) String amount) {
        return ResponseEntity.ok(requestService.requestConversionFromSource(from, to, amount));
    }

    @GetMapping("/rate")
    public ResponseEntity<Currency> getExchangeRate(@PathParam(FROM_VALUE) String from, @PathParam(TO_VALUE) String to) {
        Currency fromSource = requestService.requestCurrencyFromSource(from);
        return ResponseEntity.ok(new Currency(fromSource.getSourceCurrency(), Map.of(to, fromSource.getQuotes().getOrDefault(to, Double.NaN))));
    }

    @PutMapping("/rates")
    public ResponseEntity<Currency> selectedBySource(@PathParam(SOURCE_VALUE) String source, @RequestBody List<String> quotesList) {
        Map<String, Double> quotesByBase = requestService.requestCurrencyFromSource(source).getQuotes();
        Map<String, Double> result = new TreeMap<>();
        quotesList.forEach(quote -> result.put(quote, quotesByBase.get(quote)));
        return ResponseEntity.ok(new Currency(source, result));
    }
}
