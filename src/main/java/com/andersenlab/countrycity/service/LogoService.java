package com.andersenlab.countrycity.service;

import org.springframework.web.multipart.MultipartFile;

public interface LogoService {

    byte[] getImageAsByteArray(String id);

    String getFileName(String id);

    void updateLogo(String id, MultipartFile file);
}
