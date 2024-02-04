package com.andersenlab.countrycity.service;

import com.andersenlab.countrycity.DTO.request.CityEditDTO;
import com.andersenlab.countrycity.DTO.response.CityDTO;
import com.andersenlab.countrycity.DTO.response.CityWithLogosDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityService {
    Page<CityWithLogosDTO> getWithLogos(Pageable pageable);

    List<String> getUniqueCityNames();

    List<CityDTO> getByCountryName(String countryName);

    List<CityDTO> getByCityName(String cityName);

    CityDTO updateCityName(long id, CityEditDTO cityEditDTO);
}
