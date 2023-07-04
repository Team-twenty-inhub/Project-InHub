package com.twenty.inhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
//@ConfigurationPropertiesScan
public class InhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(InhubApplication.class, args);
	}

}
