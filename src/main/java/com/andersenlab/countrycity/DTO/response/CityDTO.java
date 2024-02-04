package com.andersenlab.countrycity.DTO.response;

import lombok.Builder;

@Builder
public record CityDTO(long id, String name, String country) {
}
