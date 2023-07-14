package com.project.claveadinamica.adpters;

import com.project.claveadinamica.configuration.RabbitMqConfig;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.OutboundMessageResult;
import reactor.rabbitmq.Sender;

@Component
public class RabbitMqEventPublisher {

    private final Sender sender;

    public RabbitMqEventPublisher(Sender sender) {
        this.sender = sender;
    }

    public Flux<OutboundMessageResult> publishTaskCreated(String toDo){
        return sender.sendWithPublishConfirms(
                Mono.just(new OutboundMessage(RabbitMqConfig.TOPIC_EXCHANGE,
                        RabbitMqConfig.ROUTING_KEY_EVENTS_TASK_CREATED,
                        toDo.getBytes())));
    }
}
