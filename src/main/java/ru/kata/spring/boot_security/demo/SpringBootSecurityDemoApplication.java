package ru.kata.spring.boot_security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.kata.spring.boot_security.demo.configs.InitConfig;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
		app.getBean(InitConfig.class).init();
	}

}
