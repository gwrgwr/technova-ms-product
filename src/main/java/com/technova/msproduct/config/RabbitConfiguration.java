package com.technova.msproduct.config;

import com.technova.product.constants.RabbitProductConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class RabbitConfiguration {
    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }


    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Exchange productExchange() {
        return new DirectExchange(RabbitProductConstants.PRODUCT_EXCHANGE, true, false);
    }

    @Bean
    public Queue queueProductSave() {
        return new Queue(RabbitProductConstants.PRODUCT_SAVE_QUEUE, true);
    }

    @Bean
    public Queue queueProductFindByIdRequest() {
        return new Queue(RabbitProductConstants.PRODUCT_GET_ID_QUEUE, true);
    }

    @Bean
    public Queue queueProductFindByNameRequest() {
        return new Queue(RabbitProductConstants.PRODUCT_GET_COMPANY_NAME_QUEUE, true);
    }

//    @Bean
//    public Queue queueProductFindByCategory() {
//        return new Queue(RabbitProductConstants.PRODUCT_GET_COMPANY_CATEGORY_QUEUE, true);
//    }

    @Bean
    public Queue queueProductFindByCompanyNameRequest() {
        return new Queue(RabbitProductConstants.PRODUCT_GET_COMPANY_NAME_QUEUE, true);
    }

    @Bean
    public Queue queueProductDeleteRequest() {
        return new Queue(RabbitProductConstants.PRODUCT_DELETE_QUEUE, true);
    }

    @Bean
    public Queue queueProductUpdateRequest() {
        return new Queue(RabbitProductConstants.PRODUCT_UPDATE_QUEUE, true);
    }

    @Bean
    public Binding bindingProductSaveRequest(Queue queueProductSave, Exchange productExchange) {
        return BindingBuilder.bind(queueProductSave).to(productExchange).with(RabbitProductConstants.PRODUCT_SAVE_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding bindingProductFindByIdRequest(Queue queueProductFindByIdRequest, Exchange productExchange) {
        return BindingBuilder.bind(queueProductFindByIdRequest).to(productExchange).with(RabbitProductConstants.PRODUCT_GET_ID_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding bindingProductFindByCompanyNameRequest(Queue queueProductFindByCompanyNameRequest, Exchange productExchange) {
        return BindingBuilder.bind(queueProductFindByCompanyNameRequest).to(productExchange).with(RabbitProductConstants.PRODUCT_GET_COMPANY_NAME_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding bindingProductDeleteRequest(Queue queueProductDeleteRequest, Exchange productExchange) {
        return BindingBuilder.bind(queueProductDeleteRequest).to(productExchange).with(RabbitProductConstants.PRODUCT_DELETE_ROUTING_KEY).noargs();
    }

    @Bean
    public Binding bindingProductUpdateRequest(Queue queueProductUpdateRequest, Exchange productExchange) {
        return BindingBuilder.bind(queueProductUpdateRequest).to(productExchange).with(RabbitProductConstants.PRODUCT_UPDATE_ROUTING_KEY).noargs();
    }

//    @Bean
//    public Binding bindingProductFindByCategory(Queue queueProductFindByCategory, Exchange productExchange) {
//        return BindingBuilder.bind(queueProductFindByCategory).to(productExchange).with(RabbitProductConstants.PRODUCT_GET_COMPANY_CATEGORY_ROUTING_KEY).noargs();
//    }
}
