package dev.printes.poll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PollApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollApplication.class, args);
	}

}
