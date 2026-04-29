package consumo.worpress.clases.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitConfig {

    public static final String DLQ_NAME = "product.created.dlq";
    public static final String DLQ_EXCHANGE = "product.dlx";
    @Bean
    public Queue productCreatedQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key",
                        "product.created.failed")
                .build();
    }
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ_NAME, true);
    }
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLQ_EXCHANGE);
    }
    @Bean
    public Binding dlqBinding(Queue deadLetterQueue, DirectExchange
            deadLetterExchange) {
        return BindingBuilder
                .bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with("product.created.failed");
    }

    // ========== CONFIGURACIÓN DE SERIAIZACIÓN ==========
    @Bean
    public MessageConverter jsonMessageConverter() {
// ↓ CRÍTICO: usar Jackson JSON en lugar de Java Serialization
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
    // ========== DECLARACIÓN DE EXCHANGES, QUEUES Y BINDINGS ==========
    public static final String EXCHANGE_NAME = "product.events";
    public static final String QUEUE_NAME = "product.created.queue";
    public static final String ROUTING_KEY = "product.created";
    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
    //    @Bean
//    public Queue productCreatedQueue() {
//        return new Queue(QUEUE_NAME, true); // durable = true
//    }
    @Bean
    public Binding binding(Queue productCreatedQueue, TopicExchange
            productExchange) {
        return BindingBuilder
                .bind(productCreatedQueue)
                .to(productExchange)
                .with(ROUTING_KEY);
    }
}
