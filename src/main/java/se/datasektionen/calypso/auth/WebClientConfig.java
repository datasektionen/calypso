package se.datasektionen.calypso.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient(
            ClientRegistrationRepository repo,
            OAuth2AuthorizedClientRepository clients) {

        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                        repo, clients);

        oauth.setDefaultOAuth2AuthorizedClient(true);

        return WebClient.builder()
                .apply(oauth.oauth2Configuration())
                .build();
    }

}
