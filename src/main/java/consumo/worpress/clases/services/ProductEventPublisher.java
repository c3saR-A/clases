package consumo.worpress.clases.services;


import consumo.worpress.clases.dto.ProductCreatedEvent;

public interface ProductEventPublisher {
    void publishProductCreated(ProductCreatedEvent event);

}
