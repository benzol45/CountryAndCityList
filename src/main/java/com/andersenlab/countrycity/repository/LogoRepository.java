package com.andersenlab.countrycity.repository;

import com.andersenlab.countrycity.entity.Logo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogoRepository extends JpaRepository<Logo, UUID> {
}
