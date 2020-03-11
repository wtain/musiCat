package ru.rz.musiCat.service;

//import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//import ru.rz.musiCat.controllers.HostController;

@SpringBootApplication(scanBasePackages = "ru.rz.musiCat")
@EnableJpaRepositories("ru.rz.musiCat.data.repositories")
//@ComponentScan("ru.rz.musiCat.data.entities")
@EntityScan("ru.rz.musiCat.data.entities")
public class Application /* extends SpringBootServletInitializer */ {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {

//      System.out.println("Let's inspect the beans provided by Spring Boot:");

//      String[] beanNames = ctx.getBeanDefinitionNames();
//      Arrays.sort(beanNames);
//      for (String beanName : beanNames) {
//        System.out.println(beanName);
//      }

    };
  }

}
