package com.apress.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.apress.spring.domain.Journal;
import com.apress.spring.repository.JournalRepository;

@SpringBootApplication
public class SimpleMongoAppApplication {

	private static final Logger log = LoggerFactory.getLogger(SimpleMongoAppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SimpleMongoAppApplication.class, args);
	}

	@Bean
	CommandLineRunner start(JournalRepository repo) {
		return args -> {
			log.info("> 기존 데이터 삭제...");
			repo.deleteAll();

			log.info("> 데이터를 새로 생성...");
			repo.save(new Journal("스프링 부트 입문", "오늘부터 스프링 부트를 배웠다", "08/22/2017"));
			repo.save(new Journal("간단한 스프링 부트 프로젝트", "스프링 부트 프로젝트를 처음 만들어보았다", "08/24/2017"));
			repo.save(new Journal("스프링 부트 해부", "스프링 부트를 자세히 살펴보았다", "09/06/2017"));
			repo.save(new Journal("스프링 부트 클라우드", "클라우드 파운드리를 응용한 스프링 부트를 공부했다", "09/25/2017"));

			log.info("> 전체 데이터 조회...");
			repo.findAll().forEach(entry -> log.info(entry.toString()));

			log.info("> LIKE로 데이터 조회...");
			repo.findByTitleLike("클라우드").forEach(entry -> log.info(entry.toString()));
		};
	}
}
