package com.santiago.posada.handler;

import com.santiago.posada.config.RabbitMqConfig;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.Receiver;

@Component
public class RabbitMqEventConsumer {

    private final Receiver receiver;

    public RabbitMqEventConsumer(Receiver receiver) {
        this.receiver = receiver;
        receiver.consumeManualAck(RabbitMqConfig.QUEUE_EVENTS_CLAVE_VALIDADA).subscribe(delivery -> {
            System.out.println("Received message: " + new String(delivery.getBody()));

            // Process the message

            if (delivery.getBody() != null) {
                // Acknowledge the message
                delivery.ack();
            } else {
                // Reject the message and requeue it
                delivery.nack(false);
            }
        });
    }

}
