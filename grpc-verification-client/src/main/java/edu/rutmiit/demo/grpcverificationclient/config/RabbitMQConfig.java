package edu.rutmiit.demo.grpcverificationclient.config;

import edu.rutmiit.demo.events.RoutingKeys;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {

    public static final String VERIFICATION_QUEUE = "q.verification.user-registered";
    public static final String VERIFICATION_DLQ = "q.verification.user-registered.dlq";

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
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(3);
        factory.setDefaultRequeueRejected(false);
        return factory;
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

    @Bean
    public Queue verificationQueue() {
        return QueueBuilder
                .durable(VERIFICATION_QUEUE)
                .deadLetterExchange(RoutingKeys.EXCHANGE + ".dlx")
                .deadLetterRoutingKey(VERIFICATION_DLQ)
                .build();
    }

    @Bean
    public Queue verificationDlq() {
        return QueueBuilder.durable(VERIFICATION_DLQ).build();
    }

    @Bean
    public Binding verificationBinding(Queue verificationQueue, TopicExchange eventsExchange) {
        return BindingBuilder
                .bind(verificationQueue)
                .to(eventsExchange)
                .with(RoutingKeys.USER_REGISTERED); // только user.registered
    }

    @Bean
    public Binding verificationDlqBinding(Queue verificationDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder
                .bind(verificationDlq)
                .to(deadLetterExchange)
                .with(VERIFICATION_DLQ);
    }
}