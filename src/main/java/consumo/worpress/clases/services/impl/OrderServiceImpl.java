package consumo.worpress.clases.services.impl;

import consumo.worpress.clases.dto.CreateProductRequest;
import consumo.worpress.clases.entity.Client;
import consumo.worpress.clases.entity.Order;
import consumo.worpress.clases.entity.Pedido;
import consumo.worpress.clases.enums.OrderStatus;
import consumo.worpress.clases.repository.ClientRepository;
import consumo.worpress.clases.repository.PedidoRepository;
import consumo.worpress.clases.services.EspoCrmService;
import consumo.worpress.clases.services.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ClientRepository clienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    // Inyección de servicios de integración (EspoCRM y WooCommerce)
    @Autowired
    private EspoCrmService espoService;

    @Autowired
    private WooCommerceServiceImpl wooCommerceService;

    @Override
    @Transactional
    public void procesarPedido(Order order) {
        try {
            // Buscamos si cliente existe por email
            Client cliente = clienteRepository.findByClientEmail(order.getClientEmail())
                    .orElseGet(() -> {
                        Client nuevo = new Client();
                        nuevo.setName(order.getClientName());
                        nuevo.setLastName(order.getClientLastName());
                        nuevo.setClientEmail(order.getClientEmail());
                        return clienteRepository.save(nuevo);
                    });

            // Guardamos pedido relacionado a cliente
            Pedido pedido = new Pedido();
            pedido.setClienteId(cliente.getId());
            pedido.setProductName(order.getProductName());
            pedido.setPrice(order.getPrice());
            pedido.setQuantity(order.getQuantity());
            pedidoRepository.save(pedido);

            //INTEGRACIONES EXTERNAS (Llamada de Espo y WP)
            espoService.createContact(
                    order.getClientName(),
                    order.getClientLastName(),
                    order.getClientEmail()
            );

            CreateProductRequest productRequest = new CreateProductRequest();
            productRequest.setName(order.getProductName());
            productRequest.setDescription(order.getProductDescription());
            productRequest.setPrice(order.getPrice());

            wooCommerceService.createProduct(productRequest);

            // Cambio a RECEIVED
            order.setOrderStatus(OrderStatus.RECEIVED);

            // Enviamos el cuerpo
            System.out.println("orden: "+ order.getId());
            rabbitTemplate.convertAndSend("order.grupoA.response.queue", order);

        } catch (Exception e) {
            System.err.println("Error procesando orden: " + e.getMessage());
            throw e;
        }
    }
}
