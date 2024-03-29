package com.differentdoors.microsoft.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfigMicrosoft {

    @Value("${different_doors.microsoft.token_url}")
    protected String accessTokenUri;
    @Value("${different_doors.microsoft.client_id}")
    protected String clientId;
    @Value("${different_doors.microsoft.client_secret}")
    protected String clientSecret;
    @Value("${different_doors.microsoft.scope}")
    protected String scope;

    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepositoryMicrosoft() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("microsoft")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tokenUri(accessTokenUri)
                .scope(scope)
                .build();
        return new InMemoryReactiveClientRegistrationRepository(clientRegistration);
    }

    @Bean("Microsoft")
    WebClient webclientMicrosoft() {
        InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepositoryMicrosoft());
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepositoryMicrosoft(), clientService);
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId("microsoft");
        return WebClient.builder()
                .baseUrl("https://graph.microsoft.com/v1.0/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(oauth)
                .build();
    }
}
