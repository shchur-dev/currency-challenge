package org.test.project.pojo.exchangeresource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("demo")
public class FreeExchangeHost extends ExchangeHost {
    @Value("${exchange_host}")
    private String host;
    private String port;

    public FreeExchangeHost() {
    }

    public String getHost() {
        return host;
    }
}
