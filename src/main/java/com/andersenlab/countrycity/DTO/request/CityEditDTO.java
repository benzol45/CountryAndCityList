package com.andersenlab.countrycity.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CityEditDTO(@NotBlank String name) {
}
