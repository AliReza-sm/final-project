package ir.maktabsharif.homeserviceprovidersystem.controller;


import ir.maktabsharif.homeserviceprovidersystem.dto.UserDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.UserFilterDto;
import ir.maktabsharif.homeserviceprovidersystem.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor

public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/users/filter")
    public ResponseEntity<List<UserDto.UserResponseDto>> filterUsers(@RequestBody UserFilterDto filterDto) {
        List<UserDto.UserResponseDto> users = managerService.filterUsers(filterDto);
        return ResponseEntity.ok(users);
    }
}
