package consumo.worpress.clases.listener;

import consumo.worpress.clases.entity.Order;
import consumo.worpress.clases.services.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = "order.grupoA.queue")
    public void consumeMessage(Order order){
        System.out.println("Mensaje recibido: " + order.getId());

        orderService.procesarPedido(order);
    }
}
