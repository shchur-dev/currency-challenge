package org.test.project.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExchangeUser {
    private String username;
    private String password;
    @Value("${access_key}")
    private String accessKey;

    public String getAccessKey() {
        return accessKey;
    }
}
