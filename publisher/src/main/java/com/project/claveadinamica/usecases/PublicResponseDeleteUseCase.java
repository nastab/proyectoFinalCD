package com.project.claveadinamica.usecases;

import com.project.claveadinamica.adpters.RabbitMqEventPublisher;
import com.project.claveadinamica.repository.model.RequestGenerar;
import com.project.claveadinamica.repository.model.RequestInscribir;
import com.project.claveadinamica.repository.model.ResponseGenerar;
import com.project.claveadinamica.service.CDService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

@Component
public class PublicResponseDeleteUseCase implements Function<RequestInscribir, Mono<Map<String, String>>> {

    private final CDService service;
    private final RabbitMqEventPublisher publisher;

    public PublicResponseDeleteUseCase(CDService service, RabbitMqEventPublisher publisher) {
        this.service = service;
        this.publisher = publisher;
    }
    @Override
    public Mono<Map<String, String>> apply(RequestInscribir req) {

        return Mono.just(req)
                .flatMap(service::eliminarCD)
                .onErrorMap(ex -> ex)
                .map(res -> {
                    System.out.println(res.toString());
                    publisher.publicarClaveEliminada(res).subscribe();
                    return res;
                });
    }



}
