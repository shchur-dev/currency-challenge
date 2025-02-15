package org.test.project.pojo;

import java.util.Map;

public record Currency(String sourceCurrency, Map<String, Double> quotes) {
}
