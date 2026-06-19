package edu.rutmiit.demo.auctionrest.config;

import edu.rutmiit.demo.events.RoutingKeys;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {

    // Очередь для результатов верификации
    public static final String USER_VERIFIED_QUEUE = "q.auction.user-verified";
    public static final String USER_VERIFIED_DLQ = "q.auction.user-verified.dlq";

    @Bean
    public MessageConverter jsonMessageConverter(JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public TopicExchange eventsExchange() {
        return ExchangeBuilder
                .topicExchange(RoutingKeys.EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder
                .directExchange(RoutingKeys.EXCHANGE + ".dlx")
                .durable(true)
                .build();
    }

    // Очередь для user.verified
    @Bean
    public Queue userVerifiedQueue() {
        return QueueBuilder
                .durable(USER_VERIFIED_QUEUE)
                .deadLetterExchange(RoutingKeys.EXCHANGE + ".dlx")
                .deadLetterRoutingKey(USER_VERIFIED_DLQ)
                .build();
    }

    @Bean
    public Queue userVerifiedDlq() {
        return QueueBuilder.durable(USER_VERIFIED_DLQ).build();
    }

    @Bean
    public Binding userVerifiedBinding(Queue userVerifiedQueue, TopicExchange eventsExchange) {
        return BindingBuilder
                .bind(userVerifiedQueue)
                .to(eventsExchange)
                .with(RoutingKeys.USER_VERIFIED);
    }

    @Bean
    public Binding userVerifiedDlqBinding(Queue userVerifiedDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder
                .bind(userVerifiedDlq)
                .to(deadLetterExchange)
                .with(USER_VERIFIED_DLQ);
    }
}