package ru.BlackAndWhite.CuteJavaPet;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ApplicationContext ctx = run(Application.class, args);

    }

}
