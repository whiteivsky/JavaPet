package ru.BlackAndWhite.CuteJavaPet;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {

//      run(Application.class, args);

        ApplicationContext ctx = run(Application.class, args);
        System.out.println("Let's inspect the beans provided by Spring Boot:");

      /*  String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }*/
    }

}
