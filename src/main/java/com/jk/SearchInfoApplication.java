package com.jk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SearchInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchInfoApplication.class, args);
    }

}
