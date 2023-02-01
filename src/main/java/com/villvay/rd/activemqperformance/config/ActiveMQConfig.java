package com.villvay.rd.activemqperformance.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

//https://spring.io/guides/gs/messaging-jms/
@Configuration
@EnableJms
@Slf4j
public class ActiveMQConfig {

  @Value("${active-mq.broker-url}")
  private String brokerUrl;

//  @Bean
//  public ConnectionFactory connectionFactory() {
//    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
//    activeMQConnectionFactory.setBrokerURL(brokerUrl);
//    activeMQConnectionFactory.setTrustedPackages(List.of("com.mailshine.springbootstandaloneactivemq"));
//    return activeMQConnectionFactory;
//  }

  @Bean
  public ConnectionFactory connectionFactory() {
    log.info("Connecting to Active MQ broker at {}", brokerUrl);
    return new ActiveMQConnectionFactory(brokerUrl);
  }

  @Bean
  public JmsListenerContainerFactory<?> topicListenerFactory(ConnectionFactory connectionFactory,
                                                             DefaultJmsListenerContainerFactoryConfigurer configurer) {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    // the configurer will use PubSubDomain from application.properties if defined or false if not
    //so setting it on the factory level need to be set after this
    configurer.configure(factory, connectionFactory);
    factory.setPubSubDomain(true);
    return factory;
  }

  @Bean
  public JmsTemplate jmsTemplate() {
    JmsTemplate jmsTemplate = new JmsTemplate();
    jmsTemplate.setConnectionFactory(connectionFactory());
    jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
    jmsTemplate.setPubSubDomain(true);  // enable for Pub Sub to topic. Not Required for Queue.
    return jmsTemplate;
  }

  @Bean
  public MessageConverter jacksonJmsMessageConverter() {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    return converter;
  }
}
