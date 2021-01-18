package com.artrans.bonos;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableEurekaClient
public class BonosApplication {

	public static void main(String[] args){
		SpringApplication.run(BonosApplication.class, args);
	}


}
