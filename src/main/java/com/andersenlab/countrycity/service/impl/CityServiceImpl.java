package com.andersenlab.countrycity.service.impl;

import com.andersenlab.countrycity.DTO.request.CityEditDTO;
import com.andersenlab.countrycity.DTO.response.CityDTO;
import com.andersenlab.countrycity.DTO.response.CityWithLogosDTO;
import com.andersenlab.countrycity.entity.City;
import com.andersenlab.countrycity.exception.CityNotFoundException;
import com.andersenlab.countrycity.mapper.CityMapper;
import com.andersenlab.countrycity.repository.CityRepository;
import com.andersenlab.countrycity.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CityWithLogosDTO> getWithLogos(Pageable pageable) {
        return cityRepository.findAll(pageable)
                .map(CityMapper::toDtoWithLogos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getUniqueCityNames() {
        return cityRepository.findUniqueCityNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityDTO> getByCountryName(String countryName) {
        return cityRepository.findAllByCountryName(countryName)
                .stream().map(CityMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityDTO> getByCityName(String cityName) {
        return cityRepository.findAllByName(cityName)
                .stream().map(CityMapper::toDto).toList();
    }

    @Override
    @Transactional
    public CityDTO updateCityName(long id, CityEditDTO cityEditDTO) {
        City city = cityRepository.findById(id).orElseThrow(() -> new CityNotFoundException(id));

        if (!city.getName().equals(cityEditDTO.name())) {
            log.info("City id={} name changing from {} to {}", id, city.getName(), cityEditDTO.name());
            city.setName(cityEditDTO.name());
            city = cityRepository.save(city);
        }

        return CityMapper.toDto(city);
    }
}
