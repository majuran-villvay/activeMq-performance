package com.villvay.rd.activemqperformance.consumer;

import com.villvay.rd.activemqperformance.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JmsConsumer {
//  @JmsListener(destination = "${active-mq.topic}")
//  public void onMessage(Message message) {
//    try{
//      ObjectMessage objectMessage = (ObjectMessage)message;
//      Employee employee = (Employee)objectMessage.getObject();
//      //do additional processing
//      log.info("Received Message: "+ employee.toString());
//    } catch(Exception e) {
//      log.error("Received Exception : "+ e);
//    }
//
//  }

  @JmsListener(destination = "${active-mq.topic}", containerFactory = "topicListenerFactory")
  public void receiveMessage(Employee email) {
    log.info("Received <" + email + ">");
  }
}