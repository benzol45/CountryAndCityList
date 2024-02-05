package com.andersenlab.countrycity.service.impl;

import com.andersenlab.countrycity.DTO.request.CityEditDTO;
import com.andersenlab.countrycity.DTO.response.CityDTO;
import com.andersenlab.countrycity.entity.City;
import com.andersenlab.countrycity.entity.Country;
import com.andersenlab.countrycity.repository.CityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {
    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    //These are trivial request passing to repository and mapping the result
   /* @Test
    void getWithLogos() {
    }

    @Test
    void getUniqueCityNames() {
    }

    @Test
    void getByCountryName() {
    }

    @Test
    void getByCityName() {
    }*/

    @Test
    @DisplayName("Test for update cities name")
    void updateCityName() {
        CityEditDTO request = CityEditDTO.builder().name("newName").build();
        when(cityRepository.findById(any())).thenReturn(Optional.of(City.builder().id(1L).name("oldName").country(Country.builder().name("1").build()).build()));
        when(cityRepository.save(any())).thenReturn(City.builder().id(1L).name("newName").country(Country.builder().name("1").build()).build());

        CityDTO cityDTO = cityService.updateCityName(1L, request);

        assertEquals(1L, cityDTO.id());
        assertEquals("newName", cityDTO.name());
        verify(cityRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test for update cities name with same as old name")
    void updateCityNameWithSameName() {
        CityEditDTO request = CityEditDTO.builder().name("oldName").build();
        when(cityRepository.findById(any())).thenReturn(Optional.of(City.builder().id(1L).name("oldName").country(Country.builder().name("1").build()).build()));

        CityDTO cityDTO = cityService.updateCityName(1L, request);

        assertEquals(1L, cityDTO.id());
        assertEquals("oldName", cityDTO.name());
        verify(cityRepository, never()).save(any());
    }
}