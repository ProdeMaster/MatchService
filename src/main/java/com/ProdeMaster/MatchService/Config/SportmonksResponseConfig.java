package com.ProdeMaster.MatchService.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Configuration
public class SportmonksResponseConfig {

    @Value("${sportmonks.baseUrl}")
    private String baseUrl;

    @Bean
    public RestClient SportmonksRestClient () {
        return RestClient
                .builder()
                .baseUrl(baseUrl)
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .build();
    }
}
