package ziwg.czy_dojade_backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@OpenAPIDefinition(info = @Info(title = "Czy dojade API", version = "1.0", description = "Gospodarka Projekt"))
@SecurityScheme(name = "czydojade", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class CzyDojadeBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(CzyDojadeBackendApplication.class, args);
	}

}
