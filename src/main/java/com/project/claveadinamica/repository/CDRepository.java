package com.project.claveadinamica.repository;

import com.project.claveadinamica.repository.model.Clave;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CDRepository extends ReactiveMongoRepository<Clave, String> {

    Mono<Clave> findBySharedkeyAndConsumidor (String sharedkey, String consumer);

}
