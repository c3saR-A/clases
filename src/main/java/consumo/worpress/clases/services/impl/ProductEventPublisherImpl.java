package consumo.worpress.clases.services.impl;

import consumo.worpress.clases.config.RabbitConfig;
import consumo.worpress.clases.dto.ProductCreatedEvent;
import consumo.worpress.clases.services.ProductEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductEventPublisherImpl implements ProductEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    public ProductEventPublisherImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishProductCreated(ProductCreatedEvent event) {
        log.info("Publicando evento ProductCreated: {}",
                event.getProductName());
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                event
        );
        log.info("Evento publicado exitosamente");
    }
}