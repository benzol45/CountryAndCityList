package com.andersenlab.countrycity.controller;

import com.andersenlab.countrycity.DTO.request.CityEditDTO;
import com.andersenlab.countrycity.DTO.response.CityDTO;
import com.andersenlab.countrycity.DTO.response.CityWithLogosDTO;
import com.andersenlab.countrycity.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cities")
@Slf4j
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping("/with-logos")
    @Operation(summary = "Get list of cities with the logos",
            description = "Browse through the paginated list of cities with the logos")
    public Page<CityWithLogosDTO> getCitiesWithLogos(@Valid @PositiveOrZero
                                                     @RequestParam(required = false, defaultValue = "0") final int page,
                                                     @Valid @Positive @Max(50)
                                                     @RequestParam(required = false, defaultValue = "10") final int size) {
        log.info("Received GET /cities/with-logos request with page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return cityService.getWithLogos(pageable);
    }

    @GetMapping("/unique")
    @Operation(summary = "Display unique cities names")
    public List<String> getUniqueCitiesNames() {
        log.info("Received GET /cities/unique");
        return cityService.getUniqueCityNames();
    }

    @GetMapping
    @Operation(summary = "Get all cities with filters",
            description = "Get all cities by country name & Search by the city name")
    public ResponseEntity<List<CityDTO>> getCitiesBySomeName(@RequestParam(name = "name", required = false) String cityName,
                                                             @RequestParam(name = "country_name", required = false) String countryName) {

        log.info("Received GET /cities with cityName={}, countryName={}", cityName, countryName);
        if ((cityName == null && countryName == null) || (cityName != null && countryName != null)) {
            return ResponseEntity.badRequest().build();
        }

        if (countryName != null) {
            return ResponseEntity.ok(cityService.getByCountryName(countryName));
        } else {
            return ResponseEntity.ok(cityService.getByCityName(cityName));
        }
    }

    @SecurityRequirement(name = "basic")
    @PreAuthorize("hasRole('EDITOR')")
    @PatchMapping("/{id}")
    @Operation(summary = "Edit the city name",
            description = "Only for EDITOR role")
    public CityDTO editCity(@PathVariable(name = "id") long id,
                            @Valid @RequestBody CityEditDTO cityEditDTO) {
        log.info("Received PATH /cities/{} with new city name {}", id, cityEditDTO.name());

        return cityService.updateCityName(id, cityEditDTO);
    }
}
