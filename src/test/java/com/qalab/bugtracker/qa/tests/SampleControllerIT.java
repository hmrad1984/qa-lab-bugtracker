package com.qalab.bugtracker.qa.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = SampleControllerIT.Initializer.class)
public class SampleControllerIT {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    static {
                System.out.println("Starting PostgreSQL Testcontainer...");
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            System.out.println("JDBC URL: " + postgresContainer.getJdbcUrl());
            System.out.println("Username: " + postgresContainer.getUsername());
            System.out.println("Password: " + postgresContainer.getPassword());
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgresContainer.getUsername(),
                    "spring.datasource.password=" + postgresContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @LocalServerPort
    private int port;

    @Test
void postAndGetSampleSuccessfully() {
    String baseUrl = "http://localhost:" + port + "/api/samples";
    RestTemplate restTemplate = new RestTemplate();

    String requestJson = """
            {
              "id": null,
              "name": "Test Sample"
            }
            """;

    // Set Content-Type header
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Create the request entity with headers and body
    HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

    // Perform POST request
    ResponseEntity<String> postResponse = restTemplate.postForEntity(baseUrl, requestEntity, String.class);
    assertThat(postResponse.getStatusCode().value()).isEqualTo(201); // Adjust if your controller returns 200

    // Perform GET request
    ResponseEntity<String> getResponse = restTemplate.getForEntity(baseUrl, String.class);
    assertThat(getResponse.getStatusCode().value()).isEqualTo(200);
    assertThat(getResponse.getBody()).contains("Test Sample");
}


}
