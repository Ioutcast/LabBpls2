package vasilkov.labbpls2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitmqConfig {
    private final RabbitMqProperties rabbitMqProperties;
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitMqProperties.getHost());
        cachingConnectionFactory.setUsername(rabbitMqProperties.getUser());
        cachingConnectionFactory.setPassword(rabbitMqProperties.getPassword());
        cachingConnectionFactory.setVirtualHost(rabbitMqProperties.getVirtualHost());
        return cachingConnectionFactory;
    }
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }
    @Bean
    public Queue myQueue() {
        return new Queue(rabbitMqProperties.getQueue());
    }
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(rabbitMqProperties.getExchange(), true, false);
    }
    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(rabbitMqProperties.getRoutingKey());
    }
}
