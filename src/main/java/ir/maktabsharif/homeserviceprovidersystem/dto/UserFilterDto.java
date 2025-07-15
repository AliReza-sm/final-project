package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.Role;
import lombok.Data;

@Data
public class UserFilterDto {

    private Role role;
    private String firstName;
    private String lastName;
    private String serviceName;
    private Double minScore;
    private Double maxScore;

}
