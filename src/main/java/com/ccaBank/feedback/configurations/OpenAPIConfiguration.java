package com.ccaBank.feedback.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8083");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Steve ZEKENG");
        myContact.setEmail("stevezekeng@gmail.com");

        Info information = new Info()
                .title("Customer Feedback & Queue Management System API")
                .version("1.0")
                .description("Cette API expose les endpoints des feedbacks clients et du systeme " +
                        "de file d'attente.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
