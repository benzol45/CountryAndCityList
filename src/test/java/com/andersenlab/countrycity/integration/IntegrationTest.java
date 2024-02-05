package com.andersenlab.countrycity.integration;

import com.andersenlab.countrycity.DTO.response.CityDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest extends TestContainerConfiguration {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private static String tempStoragePath;
    private static final String LOGO_CONTENT = "Hello world";

    @DynamicPropertySource
    static void storageProperties(DynamicPropertyRegistry registry) throws IOException {
        registry.add("application.image-storage.path", () -> tempStoragePath);
    }

    @BeforeAll
    public static void createLogoFile() throws IOException {
        tempStoragePath = Files.createTempDirectory("tmpImageStorage").toFile().getAbsolutePath();
        String filename = "ae246489-9b8f-4117-ae12-3117d1ecd7ed.JPG";
        Path file = Path.of(tempStoragePath).resolve(filename);
        Files.write(file, LOGO_CONTENT.getBytes());
    }

    @Test
    @DisplayName("Test for request unique cities names")
    void getUniqueCitiesNames() {
        List<String> expected = List.of("Chicago", "St. Petersburg", "Moscow");

        ResponseEntity<List> response = testRestTemplate.getForEntity("/cities/unique", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertThat(response.getBody()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("Test for request cities names with country name filtering")
    void getCitiesWithCountryNameFiltering() {
        List<CityDTO> expected = List.of(
                CityDTO.builder().id(1000L).name("St. Petersburg").country("USA").build(),
                CityDTO.builder().id(1001L).name("Chicago").country("USA").build());

        ResponseEntity<CityDTO[]> response = testRestTemplate.getForEntity("/cities?country_name=USA", CityDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertThat(Arrays.asList(response.getBody())).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("Test for request cities names with city name filtering")
    void getCitiesWithCityNameFiltering() {
        List<CityDTO> expected = List.of(
                CityDTO.builder().id(1000L).name("St. Petersburg").country("USA").build(),
                CityDTO.builder().id(1002L).name("St. Petersburg").country("Russia").build());

        ResponseEntity<CityDTO[]> response = testRestTemplate.getForEntity("/cities?name=St. Petersburg", CityDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertThat(Arrays.asList(response.getBody())).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("Test for request cities with logos")
    void getCitiesWithLogos() {

        ResponseEntity<Map> response = testRestTemplate.getForEntity("/cities/with-logos", Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Map> result = (List<Map>) response.getBody().get("content");
        assertEquals(4, result.size());

        List<String> citiesNames = result.stream().map(dto -> (String) dto.get("name")).collect(Collectors.toList());
        Assertions.assertThat(citiesNames).containsExactlyInAnyOrderElementsOf(List.of("Chicago", "St. Petersburg", "St. Petersburg", "Moscow"));

        assertEquals(2, result.stream()
                .filter(dto -> "Chicago".equals(dto.get("name")))
                .map(dto -> (List<String>) dto.get("logos"))
                .findAny().get().size());
    }

    @Test
    @DisplayName("Test for request logo file by id")
    void getLogoFileById() {

        ResponseEntity<byte[]> response = testRestTemplate.getForEntity("/logos/ae246489-9b8f-4117-ae12-3117d1ecd7ed", byte[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertTrue(response.getHeaders().get("Content-Disposition").get(0).contains("filename=ae246489-9b8f-4117-ae12-3117d1ecd7ed.JPG"));
        assertEquals(LOGO_CONTENT, new String(response.getBody()));
    }
}
