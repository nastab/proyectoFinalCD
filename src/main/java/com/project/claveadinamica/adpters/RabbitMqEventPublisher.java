package com.project.claveadinamica.adpters;

import com.project.claveadinamica.configuration.RabbitMqConfig;
import com.project.claveadinamica.repository.model.Clave;
import com.project.claveadinamica.repository.model.ResponseGenerar;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.OutboundMessageResult;
import reactor.rabbitmq.Sender;

import java.util.Map;

@Component
public class RabbitMqEventPublisher {

    private final Sender sender;

    public RabbitMqEventPublisher(Sender sender) {
        this.sender = sender;
    }

    public Flux<OutboundMessageResult> publicarClaveGenerada(ResponseGenerar mensaje){
        return sender.sendWithPublishConfirms(
                Mono.just(new OutboundMessage(RabbitMqConfig.TOPIC_EXCHANGE,
                        RabbitMqConfig.ROUTING_KEY_EVENTS_CLAVE_GENERADA,
                        mensaje.toString().getBytes())));
    }

    public Flux<OutboundMessageResult> publicarClaveValidada(String mensaje){
        return sender.sendWithPublishConfirms(
                Mono.just(new OutboundMessage(RabbitMqConfig.TOPIC_EXCHANGE,
                        RabbitMqConfig.ROUTING_KEY_EVENTS_CLAVE_VALIDADA,
                        mensaje.toString().getBytes())));
    }

    public Flux<OutboundMessageResult> publicarClaveEliminada(Map<String, String> map){
        return sender.sendWithPublishConfirms(
                Mono.just(new OutboundMessage(RabbitMqConfig.TOPIC_EXCHANGE,
                        RabbitMqConfig.ROUTING_KEY_EVENTS_GENERAL,
                        map.toString().getBytes())));
    }

    public Flux<OutboundMessageResult> publicarClaveInscrita(Clave clave){
        return sender.sendWithPublishConfirms(
                Mono.just(new OutboundMessage(RabbitMqConfig.TOPIC_EXCHANGE,
                        RabbitMqConfig.ROUTING_KEY_EVENTS_GENERAL,
                        clave.toString().getBytes())));
    }
}
