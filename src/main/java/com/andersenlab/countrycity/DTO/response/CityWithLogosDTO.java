package com.andersenlab.countrycity.DTO.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CityWithLogosDTO(long id, String name, String country, List<String> logos) {
}
