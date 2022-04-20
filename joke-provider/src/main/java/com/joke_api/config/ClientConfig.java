package com.joke_api.config;

import com.joke_api.ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;



@Configuration
@RequiredArgsConstructor
public class ClientConfig {

    private final ConfigProperties configProperties;

    @Bean
    public WebClient jokeWebClient(){

        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-lang","uk")
                .baseUrl(configProperties.getUrl())
                .build();
    }

}
