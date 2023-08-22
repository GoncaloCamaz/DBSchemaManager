package com.example.Backend.dto.mapper;

import com.example.Backend.dto.model.PlatformDTO;
import com.example.Backend.model.Platform;

/**
 * Transforms a Platform in a PlatformDTO
 */
public class PlatformMapper {

    public static PlatformDTO toPlatformDTO(Platform p)
    {
        PlatformDTO platformDTO = new PlatformDTO();
        platformDTO.setName(p.getName());
        platformDTO.setUrl(p.getUrl());
        platformDTO.setUsername(p.getUsername());
        platformDTO.setPassword(p.getPassword());
        platformDTO.setDescription(p.getDescription());

        return platformDTO;
    }
}
