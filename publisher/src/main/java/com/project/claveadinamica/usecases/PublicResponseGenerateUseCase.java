package com.project.claveadinamica.usecases;

import com.project.claveadinamica.adpters.RabbitMqEventPublisher;
import com.project.claveadinamica.repository.model.RequestGenerar;
import com.project.claveadinamica.repository.model.RequestInscribir;
import com.project.claveadinamica.repository.model.RequestValidar;
import com.project.claveadinamica.repository.model.ResponseGenerar;
import com.project.claveadinamica.service.CDService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class PublicResponseGenerateUseCase implements Function<RequestGenerar, Mono<ResponseGenerar>> {

    private final CDService service;
    private final RabbitMqEventPublisher publisher;

    public PublicResponseGenerateUseCase(CDService service, RabbitMqEventPublisher publisher) {
        this.service = service;
        this.publisher = publisher;
    }
    @Override
    public Mono<ResponseGenerar> apply(RequestGenerar req) {

        return Mono.just(req)
                .flatMap(service::generarClave)
                .onErrorMap(ex -> ex)
                .map(res -> {
                    System.out.println(res.toString());
                    publisher.publicarClaveGenerada(res).subscribe();
                    return res;
                });
    }



}
