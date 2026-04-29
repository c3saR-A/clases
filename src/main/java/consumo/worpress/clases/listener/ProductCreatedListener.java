package consumo.worpress.clases.listener;

import consumo.worpress.clases.config.RabbitConfig;
import consumo.worpress.clases.dto.ProductCreatedEvent;
import consumo.worpress.clases.services.EspoCrmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductCreatedListener {
    private final EspoCrmService crmService;
    public ProductCreatedListener(EspoCrmService crmService) {
        this.crmService = crmService;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handleProductCreated(ProductCreatedEvent event) {
        log.info("Mensaje recibido: ProductCreated para {}",
                event.getProductName());
        try {
            // Procesar: crear Lead en EspoCRM
            String description = String.format(
                    "Interesado en producto: %s ($%.2f)",
                    event.getProductName(),
                    event.getPrice()
            );
            crmService.createLead(
                    event.getProductName(), // firstName
                    "Automático", // lastName
                    event.getContactEmail(),
                    description
            );
            log.info("Lead creado exitosamente para {}",
                    event.getContactEmail());
        } catch (Exception e) {
            log.error("Error al procesar ProductCreatedEvent", e);
            // Aquí podrías:
            // - Reencolar el mensaje (retry)
            // - Enviar a Dead Letter Queue
            // - Notificar por email del error
        }
    }
}