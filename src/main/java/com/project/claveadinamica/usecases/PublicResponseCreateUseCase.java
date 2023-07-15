package com.project.claveadinamica.usecases;

import com.project.claveadinamica.adpters.RabbitMqEventPublisher;
import com.project.claveadinamica.repository.model.Clave;
import com.project.claveadinamica.repository.model.RequestInscribir;
import com.project.claveadinamica.service.CDService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

@Component
public class PublicResponseCreateUseCase implements Function<RequestInscribir, Mono<Clave>> {

    private final CDService service;
    private final RabbitMqEventPublisher publisher;

    public PublicResponseCreateUseCase(CDService service, RabbitMqEventPublisher publisher) {
        this.service = service;
        this.publisher = publisher;
    }
    @Override
    public Mono<Clave> apply(RequestInscribir req) {

        return Mono.just(req)
                .flatMap(service::inscribirCD)
                .onErrorMap(ex -> ex)
                .map(res -> {
                    System.out.println(res.toString());
                    publisher.publicarClaveInscrita(res).subscribe();
                    return res;
                });
    }



}
