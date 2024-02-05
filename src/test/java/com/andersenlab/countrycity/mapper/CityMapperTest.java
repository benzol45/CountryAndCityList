package com.andersenlab.countrycity.mapper;

import com.andersenlab.countrycity.DTO.response.CityDTO;
import com.andersenlab.countrycity.DTO.response.CityWithLogosDTO;
import com.andersenlab.countrycity.entity.City;
import com.andersenlab.countrycity.entity.Country;
import com.andersenlab.countrycity.entity.Logo;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CityMapperTest {
    private final String FIRST_UUID = "59839074-0da1-497f-8c19-e49b5c10a6da";
    private final String SECOND_UUID = "5f175f4a-0f3e-4ef5-86c6-c617621702f7";

    @Test
    void toDto() {
        City request = getTestCity();

        CityDTO result = CityMapper.toDto(request);

        assertEquals(1L, result.id());
        assertEquals("city_name", result.name());
        assertEquals("country_name", result.country());
    }

    @Test
    void toDtoWithLogos() {
        City request = getTestCity();

        CityWithLogosDTO result = CityMapper.toDtoWithLogos(request);

        assertEquals(1L, result.id());
        assertEquals("city_name", result.name());
        assertEquals("country_name", result.country());
        assertEquals(List.of(FIRST_UUID, SECOND_UUID), result.logos());
    }

    private City getTestCity() {
        return City.builder()
                .id(1L)
                .name("city_name")
                .country(Country.builder().id(100L).name("country_name").build())
                .logos(List.of(
                        Logo.builder().id(UUID.fromString(FIRST_UUID)).filename("1.JPG").build(),
                        Logo.builder().id(UUID.fromString(SECOND_UUID)).filename("2.png").build()
                ))
                .build();
    }

}