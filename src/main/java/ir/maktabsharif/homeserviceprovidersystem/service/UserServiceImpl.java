package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.UserDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.UserFilterDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.repository.UserRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.UserSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService{

    private final UserRepository<User> userRepository;

    public UserServiceImpl(UserRepository<User> userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto.UserResponseDto> filterUsers(UserFilterDto filterDto) {
        Specification<User> specification = Specification.not(null);
        if (filterDto.getRole() != null) {
            specification = specification.and(UserSpecification.hasRole(filterDto.getRole()));
        }
        if (filterDto.getFirstName() != null && !filterDto.getFirstName().isBlank()) {
            specification = specification.and(UserSpecification.firstNameContains(filterDto.getFirstName()));
        }
        if (filterDto.getLastName() != null && !filterDto.getLastName().isBlank()) {
            specification = specification.and(UserSpecification.lastNameContains(filterDto.getLastName()));
        }
        if (filterDto.getServiceName() != null && !filterDto.getServiceName().isBlank()) {
            specification = specification.and(UserSpecification.hasService(filterDto.getServiceName()));
        }
        if (filterDto.getMinScore() != null || filterDto.getMaxScore() != null) {
            specification = specification.and(UserSpecification.scoreIsBetween(filterDto.getMinScore(), filterDto.getMaxScore()));
        }
        List<User> users = userRepository.findAll(specification);
        return users.stream().map(UserDto::mapToUserResponseDto).toList();
    }
}
