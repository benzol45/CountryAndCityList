package com.andersenlab.countrycity.mapper;

import com.andersenlab.countrycity.DTO.response.CityDTO;
import com.andersenlab.countrycity.DTO.response.CityWithLogosDTO;
import com.andersenlab.countrycity.entity.City;

public class CityMapper {
    public static CityDTO toDto(City city) {
        return CityDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .country(city.getCountry().getName())
                .build();
    }

    public static CityWithLogosDTO toDtoWithLogos(City city) {
        return CityWithLogosDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .country(city.getCountry().getName())
                .logos(city.getLogos().stream().map(logo -> logo.getId().toString()).toList())
                .build();
    }
}
