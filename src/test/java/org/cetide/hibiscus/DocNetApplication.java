package org.cetide.hibiscus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class DocNetApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocNetApplication.class, args);
    }
} 