package ir.maktabsharif.homeserviceprovidersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class TemporaryEmailDto {

    @Data
    @AllArgsConstructor
    public static class TemporaryEmailResponse {
        private String oldEmail;
        private String newEmail;
    }

    @Data
    public static class TemporaryEmailRequest {
        private String newEmail;
    }


}
