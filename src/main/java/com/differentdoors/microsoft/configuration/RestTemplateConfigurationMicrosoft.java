package com.differentdoors.microsoft.configuration;

import com.differentdoors.microsoft.authorisation.GraphAuthorisation;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfigurationMicrosoft {
    private GraphAuthorisation authorisation;

    @Autowired
    public void setGraphAuth(GraphAuthorisation authorisation) throws Exception {
        this.authorisation = authorisation;
        authorisation.setGraphAuthorisation();
    }

    @Bean(name = "Microsoft")
    public RestTemplate restTemplate() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(15000);
        requestFactory.setReadTimeout(15000);
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        requestFactory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.getInterceptors().add(new BearerTokenAuthenticationInterceptor(authorisation.getAccessTokenByClientCredentialGrant().accessToken()));
        return restTemplate;
    }
}
