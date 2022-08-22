package com.example.carservice;

import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.Data;
import lombok.NoArgsConstructor;

@EnableDiscoveryClient
@SpringBootApplication
public class CarServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarServiceApplication.class, args);
	}
	
	@Bean
	ApplicationRunner init (CarRepository repository) {
		
		return args->{
			Stream.of("Ferrari","Nios","Indica","Hcity","Creata").forEach(name ->
			repository.save(new Car(name)));
		};
	}
	
	@Configuration
	static class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        // @formatter:off
	        http
	            .authorizeRequests().anyRequest().authenticated()
	                .and()
	            .oauth2ResourceServer().jwt();
	        // @formatter:on
	    }
	}

}

@Data
@NoArgsConstructor
@Entity
class Car {

	public Car(@NotNull String name) {
		super();
		this.name = name;
	}

	@Id
	private Long id;

	@NotNull
	private String name;

}

interface CarRepository extends JpaRepository<Car, Long>{}
