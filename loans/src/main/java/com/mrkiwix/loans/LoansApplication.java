package com.mrkiwix.loans;

import com.mrkiwix.loans.dto.LoansContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableConfigurationProperties(LoansContactInfoDto.class)
@SpringBootApplication
/*@ComponentScans({ @ComponentScan("com.Mrkiwix.loans.controller") })
@EnableJpaRepositories("com.Mrkiwix.loans.repository")
@EntityScan("com.Mrkiwix.loans.model")*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
        info = @Info(
                title = "Loans microservice REST API Documentation",
                description = "MrkiwixBank Loans microservice REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Madan Reddy",
                        email = "tutor@Mrkiwix.com",
                        url = "https://www.tenexy.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.tenexy.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "MrkiwixBank Loans microservice REST API Documentation",
                url = "https://www.tenexy.com/swagger-ui.html"
        )
)
public class LoansApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoansApplication.class, args);
	}

}
