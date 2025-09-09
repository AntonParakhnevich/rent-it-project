package com.rentit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableFeignClients
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class RentItApplication {
    
    @PostConstruct
    public void init() {
        // Установка временной зоны по умолчанию для всего приложения
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
    }
    
    public static void main(String[] args) {
        SpringApplication.run(RentItApplication.class, args);
    }
} 