package com.project.claveadinamica.repository.model;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGenerar {

    private String sharedkey;
    private String consumidor;
    private String otp;
}
