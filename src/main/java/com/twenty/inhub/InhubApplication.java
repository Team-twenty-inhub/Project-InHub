package com.twenty.inhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(InhubApplication.class, args);
	}

}
