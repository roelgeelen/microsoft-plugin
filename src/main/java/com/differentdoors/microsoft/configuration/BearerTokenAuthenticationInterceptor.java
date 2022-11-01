package com.differentdoors.microsoft.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;

public class BearerTokenAuthenticationInterceptor implements ClientHttpRequestInterceptor {
    private final String token;

    public BearerTokenAuthenticationInterceptor(String token) {
        this.token = token;
    }

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        if (!headers.containsKey("Authorization")) {
            headers.setBearerAuth(this.token);
        }

        return execution.execute(request, body);
    }
}