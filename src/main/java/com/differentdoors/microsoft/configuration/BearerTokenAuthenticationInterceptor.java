package com.differentdoors.microsoft.configuration;

import com.differentdoors.microsoft.authorisation.GraphAuthorisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;

public class BearerTokenAuthenticationInterceptor implements ClientHttpRequestInterceptor {
    private GraphAuthorisation authorisation;

    @Autowired
    public void setGraphAuth(GraphAuthorisation authorisation) throws Exception {
        this.authorisation = authorisation;
        authorisation.setGraphAuthorisation();
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        if (!headers.containsKey("Authorization")) {
            try {
                headers.setBearerAuth(authorisation.getAccessTokenByClientCredentialGrant().accessToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return execution.execute(request, body);
    }
}