package com.example.Backend.resource;

import com.example.Backend.Security.JwtTokenUtil;
import com.example.Backend.Security.request.JwtRequest;
import com.example.Backend.Security.request.JwtResponse;
import com.example.Backend.dto.model.DBSchemaManagerUserDTO;
import com.example.Backend.service.DBSchemaManagerUserService;
import com.example.Backend.utils.RequestConstants;
import com.example.Backend.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/dbschemamanager")
public class DBSchemaManagerUserResource
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    DBSchemaManagerUserService DBSchemaManagerUserService;

    @PostMapping("login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception
    {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final DBSchemaManagerUserDTO userDetails = DBSchemaManagerUserService.findByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse("Bearer "+token, userDetails.getRole()));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN') ")
    @PostMapping("register")
    public ResponseObject<?> register(@RequestBody HashMap<String, String> request)
    {
        try {
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.userrequirements)))
                return new ResponseObject<DBSchemaManagerUserDTO>(400,"","Something went wrong!","Required parameters not found!",null);
            else
            {
                DBSchemaManagerUserDTO user = getUserDTO(request);
                if(this.DBSchemaManagerUserService.findByUsername(user.getUsername()) != null)
                {
                    return new ResponseObject<DBSchemaManagerUserDTO>(409,"","Something went wrong!","Username " + user.getUsername() + " already exists",null);
                }
                else
                {
                    this.DBSchemaManagerUserService.saveUser(user);
                    return new ResponseObject<DBSchemaManagerUserDTO>(201,"New user inserted!","","",null);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseObject<DBSchemaManagerUserDTO>(500,"","Something went wrong!","Server error!", null);
    }

    private DBSchemaManagerUserDTO getUserDTO(@RequestBody HashMap<String, String> request) {
        DBSchemaManagerUserDTO user = new DBSchemaManagerUserDTO();
        user.setUsername(request.get("username"));
        user.setRole(request.get("role"));
        return user;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("update")
    public ResponseObject<?> update(@RequestBody HashMap<String, String> request)
    {
        try {
            if(!request.keySet().containsAll(Arrays.asList(RequestConstants.userrequirements)))
                return new ResponseObject<DBSchemaManagerUserDTO>(400,"","Something went wrong!","Required parameters not found!",null);

            DBSchemaManagerUserDTO user = getUserDTO(request);
            if(this.DBSchemaManagerUserService.findByUsername(user.getUsername()) != null)
            {
                this.DBSchemaManagerUserService.updateUser(user);
                return new ResponseObject<DBSchemaManagerUserDTO>(200,"User updated","","",null);
            }
            else
            {
                return new ResponseObject<DBSchemaManagerUserDTO>(409,"","Something went wrong!","User " + user.getUsername() + " not found!",null);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseObject<DBSchemaManagerUserDTO>(500,"","Something went wrong!","Server error!", null);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("delete/{username}")
    public ResponseObject<?> delete(@PathVariable("username") String username)
    {
        try {
            String user_encoded = URLDecoder.decode(username, StandardCharsets.UTF_8);

            if(DBSchemaManagerUserService.findByUsername(user_encoded) != null)
            {
                DBSchemaManagerUserDTO user = new DBSchemaManagerUserDTO();
                user.setUsername(user_encoded);
                this.DBSchemaManagerUserService.deleteUser(user);
                return new ResponseObject<DBSchemaManagerUserDTO>(200,"User deleted!","","",null);
            }
            else
            {
                return new ResponseObject<DBSchemaManagerUserDTO>(409,"","Something went wrong!","User " + user_encoded + " not found!",null);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ResponseObject<DBSchemaManagerUserDTO>(500,"","Something went wrong!","Server error!", null);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("users")
    public List<DBSchemaManagerUserDTO> getAllUsers()
    {
        return DBSchemaManagerUserService.userList();
    }
}
