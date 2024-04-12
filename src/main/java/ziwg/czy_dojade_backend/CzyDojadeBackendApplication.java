package ziwg.czy_dojade_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CzyDojadeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CzyDojadeBackendApplication.class, args);
	}

}
