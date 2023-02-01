package com.villvay.rd.activemqperformance.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import java.util.Arrays;

@Configuration
@EnableJms
public class ActiveMQConfig {

  @Value("${active-mq.broker-url}")
  private String brokerUrl;

  @Bean
  public ConnectionFactory connectionFactory(){
    ActiveMQConnectionFactory activeMQConnectionFactory  = new ActiveMQConnectionFactory();
    activeMQConnectionFactory.setBrokerURL(brokerUrl);
    activeMQConnectionFactory.setTrustedPackages(Arrays.asList("com.mailshine.springbootstandaloneactivemq"));
    return  activeMQConnectionFactory;
  }

  @Bean
  public JmsTemplate jmsTemplate(){
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
