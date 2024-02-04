package com.andersenlab.countrycity.controller;

import com.andersenlab.countrycity.service.LogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/logos")
@Slf4j
@RequiredArgsConstructor
public class LogoController {
    private final LogoService logoService;

    @GetMapping("/{id}")
    @Operation(summary = "Get logo",
            description = "Get logo file by id")
    public ResponseEntity<byte[]> getLogoById(@PathVariable(name = "id") String id) {
        log.info("Received GET /logos/{}", id);
        byte[] bytes = logoService.getImageAsByteArray(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + logoService.getFileName(id));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(bytes);
    }

    @SecurityRequirement(name = "basic")
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    @Operation(summary = "Edit the logo",
            description = "Only for EDITOR role")
    public ResponseEntity getChangeLogo(@PathVariable(name = "id") String id,
                                        @RequestPart("logo") MultipartFile file) {
        log.info("Received PATCH /logos/{} with new logo of {} bytes", id, file.getSize());

        logoService.updateLogo(id, file);
        return ResponseEntity.ok(Map.of("id", id));
    }
}
