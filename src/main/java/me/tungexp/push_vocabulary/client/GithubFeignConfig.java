package me.tungexp.push_vocabulary.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

@Component
public class GithubFeignConfig {
    @Value("${github_endpoint:https://raw.githubusercontent.com/trannguyenhan/bilingualcrawl-vietnamese-english/master/bilingualcrawl/toomva_learn.json}")
    private String endpoint;

    private final ObjectMapper json;

    public GithubFeignConfig() {
        this.json = new ObjectMapper();
    }

    @Bean
    public VocabularyResourceClient resourceClient() {
        return Feign.builder()
            .encoder(new JacksonEncoder(json))
            .decoder(new JacksonDecoder(json))
            .logger(new Slf4jLogger("resourceClient"))
            .logLevel(Logger.Level.BASIC)
            .requestInterceptor(template -> {
                template.header("Content-Type", "application/json");

            })
            .contract(new SpringMvcContract())
            .target(VocabularyResourceClient.class, endpoint);
    }

}
