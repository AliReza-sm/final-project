package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class OrderFilterDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private OrderStatus orderStatus;

    private Long serviceId;

    private Long customerId;

    private Long specialistId;
}
