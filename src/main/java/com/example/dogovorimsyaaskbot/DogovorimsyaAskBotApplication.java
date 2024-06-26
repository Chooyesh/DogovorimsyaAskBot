package com.example.dogovorimsyaaskbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@PropertySource({"application.properties"})
@SpringBootApplication
public class DogovorimsyaAskBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(DogovorimsyaAskBotApplication.class, args);
	}

}
