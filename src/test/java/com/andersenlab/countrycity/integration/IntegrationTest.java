package com.andersenlab.countrycity.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest extends TestContainerConfiguration {
    @Autowired
    private TestRestTemplate testRestTemplate;
    private static String tempStoragePath;

    @DynamicPropertySource
    static void storageProperties(DynamicPropertyRegistry registry) throws IOException {
        tempStoragePath = Files.createTempDirectory("tmpImageStorage").toFile().getAbsolutePath();
        registry.add("application.image-storage.path", () -> tempStoragePath);
    }

    @Test
    @DisplayName("test for request unique cities names")
    void getUniqueCitiesNames() {
        List<String> expected = List.of("Chicago", "St. Petersburg", "Moscow");

        ResponseEntity<List> response = testRestTemplate.getForEntity("/cities/unique", List.class);
        System.out.println(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertThat(response.getBody()).containsExactlyInAnyOrderElementsOf(expected);
    }
}
