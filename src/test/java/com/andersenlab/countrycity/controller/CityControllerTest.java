package com.andersenlab.countrycity.controller;

import com.andersenlab.countrycity.DTO.request.CityEditDTO;
import com.andersenlab.countrycity.DTO.response.CityDTO;
import com.andersenlab.countrycity.DTO.response.CityWithLogosDTO;
import com.andersenlab.countrycity.exception.CityNotFoundException;
import com.andersenlab.countrycity.service.CityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CityController.class)
@AutoConfigureMockMvc(addFilters = false)
class CityControllerTest {
    @MockBean
    private CityService cityService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Positive test for request cities with logos without sending pageable params (using default)")
    void getCitiesWithLogosWithDefaultPageable() throws Exception {
        Page<CityWithLogosDTO> response = new PageImpl<CityWithLogosDTO>(
                List.of(
                        CityWithLogosDTO.builder().id(1L).name("1").country("1").logos(List.of("1_1", "1_2")).build(),
                        CityWithLogosDTO.builder().id(2L).name("2").country("2").logos(Collections.emptyList()).build()),
                PageRequest.of(0, 10),
                2);

        when(cityService.getWithLogos(any())).thenReturn(response);

        mockMvc.perform(get("/cities/with-logos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10));

        verify(cityService, times(1)).getWithLogos(any());
    }

    @Test
    @DisplayName("Positive test for request cities with logos with empty response from service")
    void getCitiesWithLogosWithEmptyList() throws Exception {
        Page<CityWithLogosDTO> response = new PageImpl<CityWithLogosDTO>(
                Collections.emptyList(),
                PageRequest.of(0, 10),
                0);

        when(cityService.getWithLogos(any())).thenReturn(response);

        mockMvc.perform(get("/cities/with-logos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10));

        verify(cityService, times(1)).getWithLogos(any());
    }

    @Test
    @DisplayName("Positive test for request unique cities names")
    void getUniqueCitiesNames() throws Exception {
        when(cityService.getUniqueCityNames()).thenReturn(List.of("1", "3", "2"));

        mockMvc.perform(get("/cities/unique")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        verify(cityService, times(1)).getUniqueCityNames();
    }

    @Test
    @DisplayName("Positive test for request unique cities names with empty response from service")
    void getUniqueCitiesNamesWithEmptyList() throws Exception {
        when(cityService.getUniqueCityNames()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cities/unique")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(cityService, times(1)).getUniqueCityNames();
    }

    @Test
    @DisplayName("Positive test for request cities only with country name")
    void getCitiesByCountryName() throws Exception {
        when(cityService.getByCountryName("test")).thenReturn(
                List.of(CityDTO.builder().id(1L).name("1").country("1").build(),
                        CityDTO.builder().id(2L).name("2").country("2").build()));

        mockMvc.perform(get("/cities?country_name=test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(cityService, times(1)).getByCountryName("test");
        verify(cityService, never()).getByCityName(any());
    }

    @Test
    @DisplayName("Positive test for request cities only with city name")
    void getCitiesByCityName() throws Exception {
        when(cityService.getByCityName("test")).thenReturn(
                List.of(CityDTO.builder().id(1L).name("1").country("1").build(),
                        CityDTO.builder().id(2L).name("2").country("2").build()));

        mockMvc.perform(get("/cities?name=test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(cityService, times(1)).getByCityName("test");
        verify(cityService, never()).getByCountryName(any());
    }

    @Test
    @DisplayName("Negative test for request cities with both city & country name")
    void getCitiesByCityAndCountryName() throws Exception {

        mockMvc.perform(get("/cities?name=test&country_name=test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(cityService, never()).getByCityName(any());
        verify(cityService, never()).getByCountryName(any());
    }

    @Test
    @DisplayName("Positive test for edit cities with correct new city name")
    void editCity() throws Exception {
        String request = """
                {
                    "name": "new"
                }""";

        when(cityService.updateCityName(anyLong(), any(CityEditDTO.class))).thenReturn(CityDTO.builder().id(1L).name("new").country("1").build());

        mockMvc.perform(patch("/cities/1")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());

        verify(cityService, times(1)).updateCityName(eq(1L), any());
    }

    @Test
    @DisplayName("Negative test for edit cities with empty new city name")
    void editCityWithEmptyCityName() throws Exception {
        String request = """
                {
                    "name": ""
                }""";

        mockMvc.perform(patch("/cities/1")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());

        verify(cityService, never()).updateCityName(eq(1), any());
    }

    @Test
    @DisplayName("Negative test for edit cities with not founded id of city")
    void editCityWithNotFoundId() throws Exception {
        String request = """
                {
                    "name": "new"
                }""";

        when(cityService.updateCityName(anyLong(), any(CityEditDTO.class))).thenThrow(new CityNotFoundException());

        mockMvc.perform(patch("/cities/999")
                        .contentType(APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound());

        verify(cityService, times(1)).updateCityName(eq(999L), any());
    }
}