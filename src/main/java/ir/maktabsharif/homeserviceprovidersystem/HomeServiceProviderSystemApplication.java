package ir.maktabsharif.homeserviceprovidersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class HomeServiceProviderSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeServiceProviderSystemApplication.class, args);
	}
}
