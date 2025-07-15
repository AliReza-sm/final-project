package ir.maktabsharif.homeserviceprovidersystem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor

public class ErrorDetails {

    private Date timestamp;
    private String message;
    private String details;
}
