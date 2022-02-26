package com.example.demo.queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.service.EmailService;
import com.example.demo.utils.StandardJsonResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RabbitMQReceiver {

	protected final Log logger = LogFactory.getLog(getClass());
	private static final String Email_Subject = "Account Balance Information";
	private static final String Email_To = "rimi.ank@gmail.com";

	@Autowired
	private EmailService emailService;

	@RabbitListener(queues = "${truist.rabbitmq.queue}")
	public void recievedMessage(StandardJsonResponse response) {
		logger.info("Recieved Message From RabbitMQ: " + response.getData());

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String jsonString = objectMapper.writeValueAsString(response);
			System.out.println(jsonString);

			emailService.sendMail(Email_To, Email_Subject, jsonString);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Email sent !!!!");

	}

}
