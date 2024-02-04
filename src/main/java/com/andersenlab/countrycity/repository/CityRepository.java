package com.andersenlab.countrycity.repository;

import com.andersenlab.countrycity.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    @Query("SELECT DISTINCT c.name FROM City c")
    List<String> findUniqueCityNames();

    List<City> findAllByName(String name);

    List<City> findAllByCountryName(String countryName);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "logos")
    Page<City> findAll(Pageable pageable);
}
