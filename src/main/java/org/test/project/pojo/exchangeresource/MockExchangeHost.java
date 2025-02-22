package org.test.project.pojo.exchangeresource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class MockExchangeHost extends ExchangeHost {
    @Value("${mock_host}")
    private String host;
    private String port;

    public MockExchangeHost() {
    }

    public String getHost() {
        return host;
    }
}
