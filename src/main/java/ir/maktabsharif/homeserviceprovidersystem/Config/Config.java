package ir.maktabsharif.homeserviceprovidersystem.Config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ir.maktabsharif.homeserviceprovidersystem")
public class Config {

    @Bean
    public EntityManager entityManager() {
        return Persistence.createEntityManagerFactory("default").createEntityManager();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
