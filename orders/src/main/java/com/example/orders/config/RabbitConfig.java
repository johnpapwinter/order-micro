package com.example.orders.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.queue.logging.request}")
    private String loggingRequestQueue;


    @Bean
    public Queue loggingRequestQueue() {
        return new Queue(loggingRequestQueue);
    }

    @Bean
    public DirectExchange loggingExchange() {
        return new DirectExchange(loggingRequestQueue);
    }

    @Bean
    public Binding bindingLoggingRequest(Queue loggingRequestQueue, DirectExchange loggingExchange) {
        return BindingBuilder.bind(loggingRequestQueue)
                .to(loggingExchange)
                .with("rpc");
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        template.setReplyTimeout(60000);
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(mapper);
        converter.setClassMapper(typeMapper());
        converter.setCreateMessageIds(true);

        return converter;
    }

    @Bean
    public DefaultJackson2JavaTypeMapper typeMapper() {
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("com.example.orders.dto", "com.example.logging.dto");

        Map<String, Class<?>> mapping = new HashMap<>();
        mapping.put("com.example.logging.dto.ResponseDTO", com.example.orders.dto.ResponseDTO.class);
        typeMapper.setIdClassMapping(mapping);

        return typeMapper;
    }

}
