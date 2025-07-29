package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.UserDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.UserFilterDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Manager;
import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.repository.BaseRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ManagerRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.UserRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
@Transactional
public class ManagerServiceImpl extends BaseServiceImpl<Manager, Long> implements ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerServiceImpl(ManagerRepository managerRepository) {
        super(managerRepository);
        this.managerRepository = managerRepository;
    }
}
