package com.example.Backend.service;

import com.example.Backend.dto.model.PlatformDTO;

import java.util.List;

public interface PlatformService
{
    PlatformDTO savePlatform(PlatformDTO platform);

    void updatePlatform(PlatformDTO platform);

    void updatePlatformCredentials(PlatformDTO platformDTO);

    void deletePlatform(String url);

    PlatformDTO findPlatformByName(String name, boolean decryptCredentials);

    List<PlatformDTO> findAll();

    PlatformDTO findPlatformByURL(String url);
}
