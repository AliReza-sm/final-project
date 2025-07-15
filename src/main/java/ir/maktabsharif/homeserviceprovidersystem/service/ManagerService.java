package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.UserDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.UserFilterDto;

import java.util.List;

public interface ManagerService {
    List<UserDto.UserResponseDto> filterUsers(UserFilterDto filterDto);
}
