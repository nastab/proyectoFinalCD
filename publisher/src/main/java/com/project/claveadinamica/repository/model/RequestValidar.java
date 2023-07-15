package com.project.claveadinamica.repository.model;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestValidar {

    @Valid
    @NotNull(message = "sharedkey es requerida")
    private String sharedkey;
    @Valid
    @NotNull(message = "consumidor es requerido")
    private String consumidor;
    @Valid
    @NotNull(message = "otp es requerido")
    private String otp;
}
