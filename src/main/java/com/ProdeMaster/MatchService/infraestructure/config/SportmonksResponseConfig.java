package com.ProdeMaster.MatchService.infraestructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class SportmonksResponseConfig {

    @Value("${sportmonks.base_url}")
    private String baseUrl;

    // @Value("${sportmonks.token}")
    // private String token;

    @Bean
    public RestClient SportmonksRestClient() {
        return RestClient
                .builder()
                .baseUrl(baseUrl)
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                // .defaultUriVariables(Map.of("api_token", token))
                .build();
    }
}
