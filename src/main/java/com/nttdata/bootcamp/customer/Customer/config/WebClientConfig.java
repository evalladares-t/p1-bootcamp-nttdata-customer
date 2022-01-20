package com.nttdata.bootcamp.customer.Customer.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${application.endpoints.url.valid_product}")
    private String urlEndpointProduct;
    @Value("${application.endpoints.url.valid_movement}")
    private String urlEndpointMovement;

    @Bean
    public WebClient webClientProduct() {
        System.out.println(urlEndpointProduct);
        return WebClient.builder()
                .baseUrl(urlEndpointProduct)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    @Bean
    public WebClient webClientMovement() {
        return WebClient.builder()
                .baseUrl(urlEndpointMovement)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
