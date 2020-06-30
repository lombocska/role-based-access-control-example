package io.lombocska.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.DefaultRedirectStrategy;

@SpringBootApplication
public class RoleBasedAccessControlExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoleBasedAccessControlExampleApplication.class, args);
	}

	@Bean
	public DefaultRedirectStrategy defaultRedirectStrategy() {
		return new DefaultRedirectStrategy();
	}
}
