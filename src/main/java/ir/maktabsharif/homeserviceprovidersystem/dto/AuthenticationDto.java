package ir.maktabsharif.homeserviceprovidersystem.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class AuthenticationDto {

    @Data
    public static class LoginRequestDto {
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password cannot be blank")
        private String password;
    }

    @Data
    public static class LoginResponseDto {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String email;
        private String role;

        public LoginResponseDto(String accessToken, Long id, String email, String role) {
            this.token = accessToken;
            this.id = id;
            this.email = email;
            this.role = role;
        }
    }

    @Data
    public static class MessageResponse {
        private String message;
        public MessageResponse(String message) {
            this.message = message;
        }
    }


}
