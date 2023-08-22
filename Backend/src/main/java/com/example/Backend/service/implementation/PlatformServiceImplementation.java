package com.example.Backend.service.implementation;

import com.example.Backend.dto.mapper.PlatformMapper;
import com.example.Backend.dto.model.PlatformDTO;
import com.example.Backend.model.Platform;
import com.example.Backend.repository.PlatformRepo;
import com.example.Backend.service.PlatformService;
import com.example.Backend.utils.PasswordEncryptor;
import com.example.Backend.utils.PasswordEncryptorImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class PlatformServiceImplementation implements PlatformService {

    private PasswordEncryptor encryptor;

    @Autowired
    private PlatformRepo platformRepo;

    @Override
    public PlatformDTO savePlatform(PlatformDTO platformDTO) {
        Platform platform = new Platform();
        if(platformRepo.findByURL(platformDTO.getUrl()) == null)
        {
            encryptor = new PasswordEncryptorImplementation();
            platform.setName(platformDTO.getName());
            platform.setUrl(platformDTO.getUrl());
            platform.setUsername(platformDTO.getUsername());
            platform.setPassword(encryptor.encryptDataBaseObjectPassword(platformDTO.getPassword()));
            platform.setDescription(platformDTO.getDescription());
            platform.setSchemaSet(new HashSet<>());

            try {
                return PlatformMapper.toPlatformDTO(platformRepo.save(platform));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Its very important that update methods doesn't change things like name or other attributes that are used to identify
     * the object. Although a platform like every entity is defined on the database with an id, no id is sent to the outside
     * world so objects are identified by a set of attributes that must not change.
     * @param platformDTO platform from frontend
     */
    @Override
    public void updatePlatform(PlatformDTO platformDTO) {
        Platform platform = platformRepo.findByName(platformDTO.getName());
        if(platform != null)
        {
            platform.setUrl(platformDTO.getUrl());
            platform.setDescription(platformDTO.getDescription());
            try {
                platformRepo.save(platform);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updatePlatformCredentials(PlatformDTO platformDTO) {
        Platform platform = platformRepo.findByName(platformDTO.getName());
        if(platform != null)
        {
            encryptor = new PasswordEncryptorImplementation();
            platform.setUsername(platformDTO.getUsername());
            platform.setPassword(encryptor.encryptDataBaseObjectPassword(platformDTO.getPassword()));
            try {
                platformRepo.save(platform);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deletePlatform(String name) {
        Platform platform = platformRepo.findByName(name);
        if(platform!=null)
        {
            try {
                platformRepo.delete(platform);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public PlatformDTO findPlatformByName(String name, boolean decrypt) {
        if(decrypt)
        {
            Platform platform = platformRepo.findByName(name);
            encryptor = new PasswordEncryptorImplementation();
            String password = platform.getPassword();
            PlatformDTO platformDTO = PlatformMapper.toPlatformDTO(platform);
            platformDTO.setPassword(encryptor.decryptDataBaseObjectPassword(password));
            return platformDTO;
        }
        else
        {
            Platform p = platformRepo.findByName(name);
            return p != null ? PlatformMapper.toPlatformDTO(p) : null;
        }
    }

    @Override
    public List<PlatformDTO> findAll() {
        List<PlatformDTO> platformDTOS = new ArrayList<>();
        List<Platform> platforms = platformRepo.findAll();

        for(Platform p : platforms)
        {
            PlatformDTO platformDTO = PlatformMapper.toPlatformDTO(p);
            platformDTOS.add(platformDTO);
        }

        return platformDTOS;
    }

    @Override
    public PlatformDTO findPlatformByURL(String url) {
        Platform platform = platformRepo.findByURL(url);
        return platform != null ? PlatformMapper.toPlatformDTO(platform) : null;
    }
}
