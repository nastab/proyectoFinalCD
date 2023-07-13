package com.project.claveadinamica.repository.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
public class Clave {

    @Id
    private String sharedkey;

    private String consumidor;

    private LocalDateTime fechaInscripcion;

    private LocalDateTime fechaGeneracion;

    private String otp;

    private Integer intentos;

    private boolean isValidated;

    public Clave(){

    }

    public Clave(String sharedkey, String consumidor){
        this.sharedkey = sharedkey;
        this.consumidor = consumidor;
    }

    public Clave(String sharedkey, String consumidor, LocalDateTime fechaInscripcion){
        this.sharedkey = sharedkey;
        this.consumidor = consumidor;
        this.fechaInscripcion = fechaInscripcion;
    }

}
