package net.dimjasevic.karlo.fer.evidentor.devices_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "net.dimjasevic.karlo.fer.evidentor.domain")
@EnableJpaRepositories(basePackages = "net.dimjasevic.karlo.fer.evidentor.domain")
public class DevicesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevicesServiceApplication.class, args);
	}

}
