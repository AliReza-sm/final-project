package ir.maktabsharif.homeserviceprovidersystem.config;

import ir.maktabsharif.homeserviceprovidersystem.repository.ManagerRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ManagerRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan(basePackages = "ir.maktabsharif")
public class Config {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;
    }

    //login
    //update specialist
    //vaziyat specialist taghyir kone baed update
    //customer betone service va zir service haro bebine
}
