package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.UserDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.UserFilterDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends BaseService<User, Long>{

    Optional<User> findByEmail(String email);
    List<UserDto.UserResponseDto> filterUsers(UserFilterDto filterDto);
}
