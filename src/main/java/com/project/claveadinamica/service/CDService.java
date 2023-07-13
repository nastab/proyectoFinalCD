package com.project.claveadinamica.service;

import com.project.claveadinamica.repository.CDRepository;
import com.project.claveadinamica.repository.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CDService {

    @Autowired
    private CDRepository repository;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int LEFT_LIMIT = 48;
    private static final int RIGHT_LIMIT = 59;
    private static final int CHAR1 = 57;
    private static final int CHAR2 = 65;
    private static final int CHAR3 = 90;
    private static final int CHAR4 = 97;

    @Value("${app.cdsize:6}")
    private Integer size;

    @Value("${app.cdexpiration:5}")
    private Integer expirationTime;

    public Flux<Clave> getData(){
        return repository.findAll();
    }

    public Mono<Clave> inscribirCD(RequestInscribir request){
        return repository.save(new Clave(request.getSharedkey(), request.getConsumidor(), LocalDateTime.now()));
    }

    public Mono<Map<String, String>> eliminarCD(RequestInscribir request){
        return repository.findBySharedkeyAndConsumidor(request.getSharedkey(), request.getConsumidor())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El cliente no esta registrado")))
                .flatMap(cd -> {
                    repository.delete(cd).subscribe();
                    return Mono.just(Map.of("ok", "Registro eliminado correctamente"));
                });
    }

    public Mono<ResponseGenerar> generarClave(RequestGenerar request){
        return repository.findBySharedkeyAndConsumidor(request.getSharedkey(), request.getConsumidor())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El cliente no esta registrado")))
                .flatMap(cd -> {
                    cd.setOtp(generatingRandomNumeric(this.size));
                    cd.setFechaGeneracion(LocalDateTime.now());
                    repository.save(cd).subscribe();
                    return Mono.just(new ResponseGenerar(cd.getSharedkey(), cd.getConsumidor(), cd.getOtp()));
                });
    }

    public Mono<String> validarClave(RequestValidar request){
        return repository.findBySharedkeyAndConsumidor(request.getSharedkey(), request.getConsumidor())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El cliente no esta registrado")))
                .flatMap(cd -> {
                    if (cd.getFechaGeneracion() != null
                            && cd.getFechaGeneracion().isAfter(LocalDateTime.now().minusMinutes(this.expirationTime))) {
                        if (cd.getOtp().equals(request.getOtp()) && !cd.isValidated()) {
                            cd.setOtp(null);
                            cd.setFechaGeneracion(null);
                            cd.setIntentos(0);
                            cd.setValidated(true);
                            repository.save(cd).subscribe();
                            return Mono.just("El código es válido");
                        }
                        var intentos = cd.getIntentos() == null ? 0 : cd.getIntentos();
                        cd.setIntentos(intentos + 1);
                        repository.save(cd).subscribe();
                        return Mono.error(new Exception("El código no es válido"));

                    } else {
                        var intentos = cd.getIntentos() == null ? 0 : cd.getIntentos();
                        cd.setIntentos(intentos + 1);
                        repository.save(cd).subscribe();
                        return Mono.error(new Exception("El código no es válido"));
                    }
                });
    }

    private String generatingRandomNumeric(int length) {
        return RANDOM.ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
                .filter(i -> (i <= CHAR1 || i >= CHAR2) && (i <= CHAR3 || i >= CHAR4))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
