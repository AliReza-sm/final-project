package ir.maktabsharif.homeserviceprovidersystem.dto;

import lombok.Data;

public class UserDto {

    @Data
    public static class LoginRequestDto {
        private String email;
        private String password;
    }

    @Data
    public static class LoginResponseDto {
        private Long id;
        private String email;
        private String role;
    }
}
