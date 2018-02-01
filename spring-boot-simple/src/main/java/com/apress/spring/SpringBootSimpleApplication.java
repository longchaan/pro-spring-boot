package com.apress.spring;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SpringBootSimpleApplication implements ApplicationRunner, CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootSimpleApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSimpleApplication.class, args);
	}

	@Bean
	String info() {
		return "그냥 간단한 문자열 빈입니다.";
	}

	@Autowired
	String info;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("## > ApplicationRunner 구현체...");
		log.info("info 빈에 엑세스: " + info);
		args.getOptionNames().forEach(opt -> log.info(opt));
		args.getNonOptionArgs().forEach(file -> log.info(file));
	}

	@Override
	@Order
	public void run(String... args) throws Exception {
		log.info("## > CommandLineRunner 구현체...");
		log.info("info 빈에 엑세스: " + info);
		for (String arg : args)
			log.info(arg);
	}

	@Bean
	CommandLineRunner myMethod() {
		return args -> {
			log.info("## > CommandLineRunner 구현체... (자바8람다)");
			log.info("info 빈에 엑세스: " + info);
			for (String arg : args)
				log.info(arg);
		};
	}

	@Component
	class MyComponent {

		private final Logger log = LoggerFactory.getLogger(MyComponent.class);

		// @Autowired
		public MyComponent(ApplicationArguments args) {
			boolean enable = args.containsOption("enable");
			if (enable)
				log.info("## > enable 옵션을 주셨네요!");

			List<String> _args = args.getNonOptionArgs();
			log.info("## > 다른 인자 ...");
			if (!_args.isEmpty())
				_args.forEach(file -> log.info(file));
		}
	}
}
