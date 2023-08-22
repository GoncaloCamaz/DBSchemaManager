package com.example.Backend.resource;

import com.example.Backend.dto.model.PlatformDTO;
import com.example.Backend.service.DBSchemaService;
import com.example.Backend.service.PlatformService;
import com.example.Backend.utils.RequestConstants;
import com.example.Backend.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/platform")
public class PlatformResource
{
    @Autowired
    private PlatformService platformService;

    @Autowired
    private DBSchemaService schemaService;

    /**
     * Entry Point to save a platform
     * @param request parameters
     * @return Response object
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("save")
    public ResponseObject<?> savePlatform(@RequestBody HashMap<String,String> request) {

        try {
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.plataformrequirements)))
                return new ResponseObject<PlatformDTO>(400,"","Something went wrong!","Required parameters not found", null);

            PlatformDTO platform = getPlatform(request);
            if(platformService.findPlatformByName(platform.getName(), false) == null)
            {
                platformService.savePlatform(platform);
                return new ResponseObject<PlatformDTO>(201,"New platform inserted","","", null);
            }
            else
            {
                return new ResponseObject<PlatformDTO>(409,"","Something went wrong!","Platform with name " + platform.getName() +" already exists!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseObject<PlatformDTO>(500,"","Something went wrong!","Server error!", null);
    }

    /**
     * Transforms a request on a Platform DTO
     * @param request parameters
     * @return PlatformDTO
     */
    private PlatformDTO getPlatform(HashMap<String, String> request) {
        PlatformDTO platform = new PlatformDTO();
        platform.setName(request.get("name"));
        platform.setUrl(request.get("url"));
        if(request.containsKey("username") && request.containsKey("password"))
        {
            platform.setUsername(request.get("username"));
            platform.setPassword(request.get("password"));
        }
        if(request.containsKey("description"))
            platform.setDescription(request.get("description"));
        return platform;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @PostMapping("update")
    public ResponseObject<?> updatePlatform(@RequestBody HashMap<String,String> request) {

        try {
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.plataformrequirements)))
                return new ResponseObject<PlatformDTO>(400,"","Something went wrong!","Required parameters not found", null);

            PlatformDTO platform = getPlatform(request);
            platformService.updatePlatform(platform);

            return new ResponseObject<PlatformDTO>(200,"Platform updated!","","", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseObject<PlatformDTO>(500,"","Something went wrong!","Server error!", null);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("update/credentials")
    public ResponseObject<?> updatePlatformCredentials(@RequestBody HashMap<String,String> request)
    {
        try{
            PlatformDTO platformDTO = getPlatform(request);
            platformService.updatePlatformCredentials(platformDTO);
            return new ResponseObject<PlatformDTO>(200,"Platform credentials updated!","","", null);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseObject<PlatformDTO>(500,"","Something went wrong!","Server error!", null);
    }

    /**
     * Endpoint API to delete platforms
     * Since schemas might be deleted, there is sent an warning to the frontend user to associate schemas to other platform
     * before delete
     * @param platform name of the platform
     * @return response object
     */
    @PreAuthorize("hasAnyAuthority('ADMIN') || hasAnyAuthority('USER')")
    @DeleteMapping("delete/{platformname}")
    public ResponseObject<?> deletePlatform(@PathVariable("platformname") String platform){
        String platform_decoded = URLDecoder.decode(platform, StandardCharsets.UTF_8);

        if(!this.schemaService.findDBSchemasByPlatformName(platform_decoded).isEmpty())
        {
            return new ResponseObject<PlatformDTO>(409,"","Something went wrong!","There are schemas associated to this platform.",null);
        }
        else
        {
            PlatformDTO platformDTO = this.platformService.findPlatformByName(platform_decoded, false);
            this.platformService.deletePlatform(platformDTO.getName());
            return new ResponseObject<PlatformDTO>(200,"Platform deleted","","",null);
        }

    }

    /**
     * Gets all platforms
     * @return list with platforms
     */
    @GetMapping("")
    public List<PlatformDTO> getAllPlatforms()
    {
        return this.platformService.findAll();
    }

    @GetMapping("url/{url}")
    public PlatformDTO getPlatformByURL(@PathVariable("url") String url)
    {
        try{
            String platform_decoded = URLDecoder.decode(url, StandardCharsets.UTF_8);
            return this.platformService.findPlatformByURL(platform_decoded);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets platform by name
     * @param name name of the platform
     * @return platform
     */
    @GetMapping("name/{name}")
    public PlatformDTO getPlatformByName(@PathVariable("name") String name)
    {
        try{
            String platform_decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);
            return this.platformService.findPlatformByName(platform_decoded, false);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets a platform with credentials
     * @param name of the platform
     * @return platform
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("credentials/name/{name}")
    public PlatformDTO getPlatformWithCredentialsByName(@PathVariable("name") String name)
    {
        try{
            String platform_decoded = URLDecoder.decode(name, StandardCharsets.UTF_8);
            return this.platformService.findPlatformByName(platform_decoded, true);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
