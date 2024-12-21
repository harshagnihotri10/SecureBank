package com.app.Bank_Management;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title ="Bank Management System",
				description = "Backend APIs for Bank.",
				version = "v1.0",
				contact = @Contact(
						name = "Izna",
						email = "izna.demomail@gmail.com",
						url= ""
		),
				license = @License(
						name ="Bank Management System",
						url =" "
				)
		),
		externalDocs = @ExternalDocumentation(
				description = " Bank Management App Documentation.",
				url = " "
		)
)
public class BankManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankManagementApplication.class, args);
	}

}
