package com.apress.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.SendTo;

@SpringBootApplication
public class SpringBootJmsV2Application {

	private static final Logger log = LoggerFactory.getLogger(SpringBootJmsV2Application.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJmsV2Application.class, args);
	}

	@JmsListener(destination = "${myqueue}")
	@SendTo("${myotherqueue}")
	public String simplerConsumer(String message) {
		log.info("간단한 소비기 > \"" + message + "\"");
		return message + " 와 스프링 메시징을 시작!";
	}

	@JmsListener(destination = "${myotherqueue}")
	public void anotherSimplerConsumer(String message) {
		log.info("Another Simpler Consumer > " + message);
	}

	@Value("${myqueue}")
	String queue;

	@Bean
	CommandLineRunner start(JmsTemplate template) {
		return args -> {
			String message = "스프링 부트";
			log.info("전송 > " + message);
			template.convertAndSend(queue, message);
		};
	}
}
