package de.uniwue.dachs.fotolyrik_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FotolyrikBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FotolyrikBackendApplication.class, args);
	}

}
