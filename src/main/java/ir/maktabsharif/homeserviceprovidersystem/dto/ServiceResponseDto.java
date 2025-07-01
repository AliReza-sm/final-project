package ir.maktabsharif.homeserviceprovidersystem.dto;

public record ServiceResponseDto(
        Long id,
        String name,
        String description,
        Double basePrice,
        Long parentServiceId
) {
}
