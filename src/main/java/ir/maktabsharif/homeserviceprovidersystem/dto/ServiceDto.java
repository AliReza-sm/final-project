package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.Service;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class ServiceDto {

    @Data
    public static class ServiceRequestDto{
        @NotBlank
        private String name;
        private String description;
        @Positive
        private Double basePrice;
        private Long parentServiceId;
    }

    @Data
    public static class ServiceResponseDto{
        private Long id;
        private String name;
        private String description;
        private Double basePrice;
        private Long parentServiceId;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ServiceUpdateDto extends ServiceResponseDto{
    }

    public static Service mapToEntity(ServiceRequestDto serviceRequestDto){
        Service service = new Service();
        service.setName(serviceRequestDto.getName());
        service.setDescription(serviceRequestDto.getDescription());
        service.setBasePrice(serviceRequestDto.getBasePrice());
        return service;
    }

    public static ServiceResponseDto mapToDto(Service service){
        if(service==null)return null;
        ServiceResponseDto serviceResponseDto = new ServiceResponseDto();
        serviceResponseDto.setId(service.getId());
        serviceResponseDto.setName(service.getName());
        serviceResponseDto.setDescription(service.getDescription());
        serviceResponseDto.setBasePrice(service.getBasePrice());
        if (service.getParentService() != null){
            serviceResponseDto.setParentServiceId(service.getParentService().getId());
        }
        return serviceResponseDto;
    }
}
