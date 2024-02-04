package com.andersenlab.countrycity.service.impl;

import com.andersenlab.countrycity.entity.Logo;
import com.andersenlab.countrycity.exception.LogoNotFoundException;
import com.andersenlab.countrycity.repository.LogoRepository;
import com.andersenlab.countrycity.service.LogoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoServiceImpl implements LogoService {
    private final LogoRepository logoRepository;
    private final Path storage;

    @Override
    @Transactional(readOnly = true)
    public byte[] getImageAsByteArray(String id) {
        String fileName = getFileName(id);

        Path logoFilePath = storage.resolve(fileName);
        if (Files.notExists(logoFilePath)) {
            log.warn("File with id={} is present in DB but not found in storage folder", id);
            throw new LogoNotFoundException();
        }

        try {
            log.info("File {} was returned", logoFilePath.toString());
            return Files.readAllBytes(logoFilePath);
        } catch (IOException ex) {
            log.warn("Can't read file {}", logoFilePath.toString());
            throw new LogoNotFoundException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getFileName(String id) {
        Logo logo = logoRepository.findById(UUID.fromString(id)).orElseThrow(() -> new LogoNotFoundException(id));

        return logo.getFilename();
    }

    @Override
    @Transactional
    public void updateLogo(String id, MultipartFile file) {
        Logo logo = logoRepository.findById(UUID.fromString(id)).orElseThrow(() -> new LogoNotFoundException(id));
        String oldLogoFileName = logo.getFilename();

        String filename = file.getOriginalFilename();
        String extension = Arrays.asList(filename.split("\\.")).getLast();
        String newLogoFileName = logo.getId() + "." + extension.toUpperCase();
        Path newLogoFile = storage.resolve(newLogoFileName);

        try {
            log.info("Writing logo {} to file {}", id, newLogoFile.toString());
            Files.write(newLogoFile, file.getBytes());
            if (!oldLogoFileName.equals(newLogoFileName)) {
                log.info("Deleting old logo {} file {}", id, oldLogoFileName);
                Files.delete(storage.resolve(oldLogoFileName));
            }
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }

        logo.setFilename(newLogoFileName);
        logoRepository.save(logo);
    }
}
