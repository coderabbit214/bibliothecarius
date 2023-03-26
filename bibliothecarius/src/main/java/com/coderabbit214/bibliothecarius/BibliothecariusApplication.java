package com.coderabbit214.bibliothecarius;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Mr_J
 */
@SpringBootApplication
@EnableAsync
public class BibliothecariusApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliothecariusApplication.class, args);
    }

}
