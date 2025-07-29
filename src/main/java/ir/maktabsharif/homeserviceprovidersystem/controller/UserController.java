package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.UserDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.UserFilterDto;
import ir.maktabsharif.homeserviceprovidersystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<List<UserDto.UserResponseDto>> filterUsers(UserFilterDto filterDto) {
        List<UserDto.UserResponseDto> users = userService.filterUsers(filterDto);
        return ResponseEntity.ok(users);
    }
}
