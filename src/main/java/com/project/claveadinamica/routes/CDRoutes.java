package com.project.claveadinamica.routes;

import com.project.claveadinamica.repository.model.Clave;
import com.project.claveadinamica.repository.model.RequestGenerar;
import com.project.claveadinamica.repository.model.RequestInscribir;
import com.project.claveadinamica.repository.model.RequestValidar;
import com.project.claveadinamica.service.CDService;
import com.project.claveadinamica.validation.ObjectValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
@RequiredArgsConstructor
public class CDRoutes {
    @Autowired
    private CDService service;

    private final ObjectValidator objectValidator;

    @Bean
    public RouterFunction<ServerResponse> getAll(){
        return route(GET("route/getAll"),
                request -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(service.getData(), Clave.class)));
    }

    @Bean
    public RouterFunction<ServerResponse> inscribirCD(){
        return route(POST("route/inscribir/cd"),
                request -> request.bodyToMono(RequestInscribir.class)
                        .doOnNext(objectValidator::validate)
                        .flatMap(r -> service.inscribirCD(r))
                        .flatMap(result -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(result))
                        .onErrorResume(error -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Map.of("error", error.getMessage()))));
    }

    @Bean
    public RouterFunction<ServerResponse> eliminarCD(){
        return route(POST("route/eliminar/cd"),
                request -> request.bodyToMono(RequestInscribir.class)
                        .doOnNext(objectValidator::validate)
                        .flatMap(r -> service.eliminarCD(r))
                        .flatMap(result -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(result))
                        .onErrorResume(error -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Map.of("error", error.getMessage()))));
    }

    @Bean
    public RouterFunction<ServerResponse> validarCD(){
        return route(POST("route/validar/cd"),
                request -> request.bodyToMono(RequestValidar.class)
                        .doOnNext(objectValidator::validate)
                        .flatMap(r -> service.validarClave(r))
                        .flatMap(result -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Map.of("ok",result)))
                        .onErrorResume(error -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Map.of("error", error.getMessage()))));
    }

    @Bean
    public RouterFunction<ServerResponse> generarCD(){
        return route(POST("route/generar/cd"),
                request -> request.bodyToMono(RequestGenerar.class)
                        .doOnNext(objectValidator::validate)
                        .flatMap(r -> service.generarClave(r))
                        .flatMap(result -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(result))
                        .onErrorResume(error -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Map.of("error", error.getMessage()))));
    }

    /*@Bean
    public RouterFunction<ServerResponse> getUserWithTasks(){
        return route(GET("route/get/user/{userId}"),
                request -> service.findAuthorWithAllHisTasks(Integer
                                .parseInt(request.pathVariable("userId")))
                        .flatMap(author -> ServerResponse.ok().bodyValue(author)));
    }*/

}
