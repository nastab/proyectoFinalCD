package com.project.claveadinamica.usecases;
import com.project.claveadinamica.adpters.RabbitMqEventPublisher;
import com.project.claveadinamica.repository.model.Clave;
import com.project.claveadinamica.repository.model.RequestValidar;
import com.project.claveadinamica.service.CDService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class PublicResponseValidateUseCase implements Function<RequestValidar, Mono<String>> {

    private final CDService toDoRepository;
    private final RabbitMqEventPublisher publisher;

    public PublicResponseValidateUseCase(CDService toDoRepository, RabbitMqEventPublisher publisher) {
        this.toDoRepository = toDoRepository;
        this.publisher = publisher;
    }
    @Override
    public Mono<String> apply(RequestValidar req) {

        return Mono.just(req)
                .flatMap(toDoRepository::validarClave)
                .onErrorMap(ex -> ex)
                .map(res -> {
                    System.out.println(res.toString());
                    publisher.publicarClaveValidada(res).subscribe();
                    return res;
                });
    }



}
