package com.joke_api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "securetracktraining.jokeprovider")
public class ConfigProperties {

    private int grpcPort;

}
