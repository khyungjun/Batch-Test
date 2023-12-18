package com.batch.test.springboot;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 참조 : https://minholee93.tistory.com/category/Spring/Batch, https://jojoldu.tistory.com/324

@EnableBatchProcessing  // 배치 기능 활성화
@SpringBootApplication
public class BatchTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchTestApplication.class, args);
	}

}
