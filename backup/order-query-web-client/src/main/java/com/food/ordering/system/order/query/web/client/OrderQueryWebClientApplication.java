package com.food.ordering.system.order.query.web.client;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.food.ordering.system")
public class OrderQueryWebClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderQueryWebClientApplication.class, args);
    }
}
