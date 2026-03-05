package com.monprojet.boutiquejeux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BoutiqueJeuxApplication {
    public static void main(String[] args) {
        SpringApplication.run(BoutiqueJeuxApplication.class, args);
    }
}
